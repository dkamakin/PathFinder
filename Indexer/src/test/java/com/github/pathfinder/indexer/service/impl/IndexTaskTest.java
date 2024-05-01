package com.github.pathfinder.indexer.service.impl;

import static com.github.pathfinder.indexer.configuration.IndexerFixtures.VERIFICATION_TIMEOUT;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.github.pathfinder.indexer.configuration.IndexerConfiguration;
import com.github.pathfinder.indexer.configuration.IndexerIntegrationTest;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.Indexer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@IndexerIntegrationTest
@Import({IndexTask.class, IndexerConfiguration.class})
class IndexTaskTest {

    @MockBean
    Indexer indexer;

    @Autowired
    IndexTask target;

    IndexBoxEntity indexBoxEntity() {
        return mock(IndexBoxEntity.class);
    }

    void whenNeedToThrowOnProcess(IndexBoxEntity entity, RuntimeException expected) {
        doThrow(expected).when(indexer).process(entity);
    }

    @Test
    void accept_HappyPath_CallIndexer() {
        var expected = indexBoxEntity();

        target.accept(expected);

        verify(indexer, VERIFICATION_TIMEOUT).process(expected);
    }

    @Test
    void accept_FailedToProcess_NoException() {
        var expected = indexBoxEntity();

        whenNeedToThrowOnProcess(expected, new IllegalArgumentException());

        target.accept(expected);

        verify(indexer, VERIFICATION_TIMEOUT).process(expected);
    }

}