package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.configuration.IndexerRetryConfiguration;
import com.github.pathfinder.indexer.configuration.IndexerServiceDatabaseTest;
import com.github.pathfinder.indexer.configuration.IndexerStateBuilder;
import com.github.pathfinder.indexer.configuration.IndexerStateBuilderConfiguration;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.BoxSearcherService;
import com.github.pathfinder.indexer.service.osm.impl.OsmIndexTask;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.ConnectChunkMessage;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@IndexerServiceDatabaseTest
@IndexerStateBuilderConfiguration
@Import({OsmIndexActor.class, IndexBoxSearcherService.class, IndexerRetryConfiguration.class})
class OsmIndexActorTest {

    @Autowired
    OsmIndexActor target;

    @Autowired
    BoxSearcherService boxSearcherService;

    @MockBean
    SearcherApi searcherApi;

    @Autowired
    IndexerStateBuilder stateBuilder;

    @Autowired
    IndexerRetryConfiguration retryConfiguration;

    @MockBean
    OsmIndexTask indexTask;

    @MockBean
    IDateTimeSupplier dateTimeSupplier;

    void whenNeedToGetNow(Instant now) {
        when(dateTimeSupplier.now()).thenReturn(now);
    }

    @Test
    void perform_BoxesNotFound_NoAction() {
        target.perform();

        verifyNoInteractions(searcherApi);
        verifyNoInteractions(indexTask);
    }

    @Test
    void perform_BoxNeedsToBeSaved_SendToSaveButNotForConnection() {
        var entity = stateBuilder.save(IndexBoxEntity.builder()
                                               .min(1, 2)
                                               .max(3, 4)
                                               .saved(false).build());

        target.perform();

        verify(indexTask).accept(entity);
        verifyNoInteractions(searcherApi);
    }

    @Test
    void perform_BoxWasSavedButNotConnected_SendToConnection() {
        var expected = stateBuilder.save(IndexBoxEntity.builder()
                                                 .min(1, 2)
                                                 .max(3, 4)
                                                 .connected(false)
                                                 .saved(true).build());
        stateBuilder.save(IndexBoxEntity.builder()
                                  .min(1, 2)
                                  .max(3, 4)
                                  .connected(true)
                                  .saved(true).build());
        var now = Instant.now();

        whenNeedToGetNow(now);

        target.perform();

        verifyNoInteractions(indexTask);
        verify(searcherApi).createConnections(new ConnectChunkMessage(expected.getId()));

        assertThat(boxSearcherService.all())
                .filteredOn(entity -> expected.getId().equals(entity.getId()))
                .hasSize(1)
                .first()
                .matches(Predicate.not(IndexBoxEntity::isConnected))
                .matches(IndexBoxEntity::isSaved)
                .satisfies(entity -> assertThat(entity.getConnectionRequestTimestamp())
                        .isCloseTo(now, within(1, ChronoUnit.SECONDS)));
    }

    @Test
    void perform_FoundOneForConnectionSecondForSave_DoNotConnectIfSomeOfTheBoxesAreNotSaved() {
        var forSave = stateBuilder.save(IndexBoxEntity.builder()
                                                .min(1, 2)
                                                .max(3, 4)
                                                .connected(false)
                                                .saved(false).build());
        var forConnection = stateBuilder.save(IndexBoxEntity.builder()
                                                      .min(1, 2)
                                                      .max(3, 4)
                                                      .connected(false)
                                                      .saved(true).build());
        var now = Instant.now();

        whenNeedToGetNow(now);

        target.perform();

        verify(indexTask).accept(forSave);
        verify(searcherApi, never()).createConnections(any());

        assertThat(boxSearcherService.all())
                .filteredOn(entity -> forConnection.getId().equals(entity.getId()))
                .hasSize(1)
                .first()
                .matches(Predicate.not(IndexBoxEntity::isConnected))
                .matches(IndexBoxEntity::isSaved)
                .matches(entity -> entity.getConnectionRequestTimestamp() == null);
    }

}
