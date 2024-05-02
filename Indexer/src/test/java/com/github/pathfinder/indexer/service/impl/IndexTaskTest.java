package com.github.pathfinder.indexer.service.impl;

import java.time.Instant;
import static com.github.pathfinder.indexer.configuration.IndexerFixtures.VERIFICATION_TIMEOUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.configuration.IndexerConfiguration;
import com.github.pathfinder.indexer.configuration.IndexerServiceDatabaseTest;
import com.github.pathfinder.indexer.configuration.IndexerStateBuilder;
import com.github.pathfinder.indexer.configuration.IndexerStateBuilderConfiguration;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.Indexer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@IndexerServiceDatabaseTest
@IndexerStateBuilderConfiguration
@Import({IndexTask.class, IndexerConfiguration.class, IndexBoxSearcherService.class})
class IndexTaskTest {

    @MockBean
    Indexer indexer;

    @Autowired
    IndexerStateBuilder stateBuilder;

    @Autowired
    IndexBoxSearcherService boxSearcherService;

    @Autowired
    IndexTask target;

    @MockBean
    IDateTimeSupplier dateTimeSupplier;

    void whenNeedToGetNow(Instant now) {
        when(dateTimeSupplier.now()).thenReturn(now);
    }

    IndexBoxEntity indexBoxEntity() {
        return stateBuilder.save(IndexBoxEntity.builder()
                                         .max(12, 23)
                                         .min(32, 43)
                                         .build());
    }

    void whenNeedToThrowOnProcess(int boxId, RuntimeException expected) {
        doThrow(expected).when(indexer).process(boxId);
    }

    @Test
    void accept_HappyPath_CallIndexer() {
        var now      = Instant.now();
        var expected = indexBoxEntity();

        whenNeedToGetNow(now);

        target.accept(expected);

        assertThat(boxSearcherService.box(expected.getId()))
                .get()
                .matches(x -> !x.isSaved())
                .matches(x -> !x.isConnected())
                .matches(x -> x.getConnectionRequestTimestamp() == null)
                .matches(x -> x.getSaveRequestTimestamp() != null);

        verify(indexer, VERIFICATION_TIMEOUT).process(expected.getId());
    }

    @Test
    void accept_FailedToProcess_NoException() {
        var expected = indexBoxEntity();

        whenNeedToThrowOnProcess(expected.getId(), new IllegalArgumentException());

        target.accept(expected);

        assertThat(boxSearcherService.box(expected.getId()))
                .get()
                .matches(x -> !x.isSaved())
                .matches(x -> !x.isConnected())
                .matches(x -> x.getConnectionRequestTimestamp() == null)
                .matches(x -> x.getSaveRequestTimestamp() == null);

        verify(indexer, VERIFICATION_TIMEOUT).process(expected.getId());
    }

}