package com.github.pathfinder.indexer.service.osm.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.configuration.IndexerServiceDatabaseTest;
import com.github.pathfinder.indexer.configuration.IndexerStateBuilder;
import com.github.pathfinder.indexer.configuration.IndexerStateBuilderConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmConfigurationProperties;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.exception.IndexBoxNotFoundException;
import com.github.pathfinder.indexer.service.impl.IndexBoxSearcherService;
import com.github.pathfinder.searcher.api.SearcherApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@IndexerServiceDatabaseTest
@IndexerStateBuilderConfiguration
@Import({OsmIndexer.class, IndexBoxSearcherService.class, OsmConfigurationProperties.class})
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

    @Test
    void process_HappyPath_IndexBox() {
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

        target.process(expected.getId());

        verify(searcherApi).save(any());
    }

    @Test
    void process_BoxIsNotSavableAnymore_SkipProcessing() {
        var expected = stateBuilder.save(IndexBoxEntity.builder()
                                                 .saved(true)
                                                 .connected(false)
                                                 .max(12, 23)
                                                 .min(32, 43)
                                                 .build());

        target.process(expected.getId());

        verifyNoInteractions(osmClient);
        verifyNoInteractions(searcherApi);
    }

    @Test
    void process_BoxDoesNotExist_IndexBoxNotFoundException() {
        assertThatThrownBy(() -> target.process(123)).isInstanceOf(IndexBoxNotFoundException.class);
    }

}