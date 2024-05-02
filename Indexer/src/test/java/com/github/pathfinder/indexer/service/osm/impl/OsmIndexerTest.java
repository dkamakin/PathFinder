package com.github.pathfinder.indexer.service.osm.impl;

import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.configuration.IndexerRetryConfiguration;
import com.github.pathfinder.indexer.configuration.IndexerServiceDatabaseTest;
import com.github.pathfinder.indexer.configuration.IndexerStateBuilder;
import com.github.pathfinder.indexer.configuration.IndexerStateBuilderConfiguration;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.impl.IndexBoxSearcherService;
import com.github.pathfinder.searcher.api.SearcherApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@IndexerServiceDatabaseTest
@IndexerStateBuilderConfiguration
@Import({OsmIndexer.class, IndexBoxSearcherService.class, IndexerRetryConfiguration.class})
class OsmIndexerTest {

    @Autowired
    OsmIndexer target;

    @MockBean
    OsmPointExtractor pointExtractor;

    @Autowired
    IndexerStateBuilder stateBuilder;

    @MockBean
    OsmClient osmClient;

    @MockBean
    SearcherApi searcherApi;

    @MockBean
    IDateTimeSupplier dateTimeSupplier;

    @Autowired
    IndexBoxSearcherService indexBoxSearcherService;

    void whenNeedToGetNow(Instant now) {
        when(dateTimeSupplier.now()).thenReturn(now);
    }

    void whenNeedToThrowOnSaveRequest(RuntimeException expected) {
        doThrow(expected).when(searcherApi).save(any());
    }

    @Test
    void process_FailToSendRequest_TimestampIsNotUpdated() {
        var exception = new IllegalArgumentException();

        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(true)
                                  .connected(true)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());
        var expected = stateBuilder.save(IndexBoxEntity.builder()
                                                 .saved(false)
                                                 .connected(false)
                                                 .max(12, 23)
                                                 .min(32, 43)
                                                 .build());

        whenNeedToThrowOnSaveRequest(exception);

        assertThatThrownBy(() -> target.process(expected)).isEqualTo(exception);

        var actual = indexBoxSearcherService.box(expected.getId());

        assertThat(actual)
                .get()
                .matches(x -> x.getSaveRequestTimestamp() == null);

        verify(searcherApi).save(any());
    }

    @Test
    void process_BoxIsSavable_IndexBox() {
        var now = Instant.now();

        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(true)
                                  .connected(true)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());
        var expected = stateBuilder.save(IndexBoxEntity.builder()
                                                 .saved(false)
                                                 .connected(false)
                                                 .max(12, 23)
                                                 .min(32, 43)
                                                 .build());

        whenNeedToGetNow(now);

        target.process(expected);

        var actual = indexBoxSearcherService.box(expected.getId());

        assertThat(actual)
                .get()
                .matches(x -> x.getSaveRequestTimestamp().equals(now));

        verify(searcherApi).save(any());
    }

    @Test
    void process_BoxIsNotSavableAnymore_SkipProcessing() {
        var now = Instant.now();

        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(true)
                                  .connected(true)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());
        var expected = stateBuilder.save(IndexBoxEntity.builder()
                                                 .saveRequestTimestamp(now)
                                                 .saved(false)
                                                 .connected(false)
                                                 .max(12, 23)
                                                 .min(32, 43)
                                                 .build());

        target.process(expected);

        verifyNoInteractions(osmClient);
    }

}