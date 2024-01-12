package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.executor.PlatformExecutor;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.configuration.IndexerRetryConfiguration;
import com.github.pathfinder.indexer.configuration.IndexerServiceDatabaseTest;
import com.github.pathfinder.indexer.configuration.IndexerStateBuilder;
import com.github.pathfinder.indexer.data.index.IndexBox;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.BoxService;
import com.github.pathfinder.indexer.service.osm.impl.OsmIndexTask;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.ConnectChunksMessage;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@IndexerServiceDatabaseTest
@Import({OsmIndexActor.class, IndexBoxService.class, IndexerRetryConfiguration.class})
class OsmIndexActorTest {

    @Autowired
    OsmIndexActor target;

    @Autowired
    BoxService boxService;

    @MockBean
    SearcherApi searcherApi;

    @Autowired
    IndexerStateBuilder stateBuilder;

    @Autowired
    IndexerRetryConfiguration retryConfiguration;

    @MockBean
    PlatformExecutor executor;

    @MockBean
    OsmIndexTask indexTask;

    @MockBean
    IDateTimeSupplier dateTimeSupplier;

    @Captor
    ArgumentCaptor<Runnable> runnableCaptor;

    void whenNeedToGetNow(Instant now) {
        when(dateTimeSupplier.now()).thenReturn(now);
    }

    @Test
    void perform_BoxesNotFound_NoAction() {
        target.perform();

        verifyNoInteractions(searcherApi);
        verifyNoInteractions(executor);
    }

    @Test
    void perform_BoxNeedsToBeSaved_SendToSaveButNotForConnection() {
        var entity = stateBuilder.save(IndexBoxEntity.builder()
                                               .min(1, 2)
                                               .max(3, 4)
                                               .saved(false).build());

        target.perform();

        verify(executor).submit(runnableCaptor.capture());

        runnableCaptor.getAllValues().forEach(Runnable::run);

        verify(indexTask).accept(new IndexBox(entity.getId(),
                                              new Coordinate(entity.getMin().getLatitude(),
                                                             entity.getMin().getLongitude()),
                                              new Coordinate(entity.getMax().getLatitude(),
                                                             entity.getMax().getLongitude())));

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

        verifyNoInteractions(executor);
        verify(searcherApi).createConnections(new ConnectChunksMessage(List.of(expected.getId())));

        assertThat(boxService.all())
                .filteredOn(entity -> expected.getId().equals(entity.getId()))
                .hasSize(1)
                .first()
                .matches(Predicate.not(IndexBoxEntity::isConnected))
                .matches(IndexBoxEntity::isSaved)
                .satisfies(entity -> assertThat(entity.getConnectionRequestTimestamp())
                        .isCloseTo(now, within(1, ChronoUnit.SECONDS)));
    }

    @Test
    void perform_FoundOneForConnectionSecondForSave_PerformOperations() {
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

        verify(executor).submit(runnableCaptor.capture());

        runnableCaptor.getAllValues().forEach(Runnable::run);

        verify(indexTask).accept(new IndexBox(forSave.getId(),
                                              new Coordinate(forSave.getMin().getLatitude(),
                                                             forSave.getMin().getLongitude()),
                                              new Coordinate(forSave.getMax().getLatitude(),
                                                             forSave.getMax().getLongitude())));

        verify(searcherApi).createConnections(new ConnectChunksMessage(List.of(forConnection.getId())));

        assertThat(boxService.all())
                .filteredOn(entity -> forConnection.getId().equals(entity.getId()))
                .hasSize(1)
                .first()
                .matches(Predicate.not(IndexBoxEntity::isConnected))
                .matches(IndexBoxEntity::isSaved)
                .satisfies(entity -> assertThat(entity.getConnectionRequestTimestamp())
                        .isCloseTo(now, within(1, ChronoUnit.SECONDS)));
    }

}
