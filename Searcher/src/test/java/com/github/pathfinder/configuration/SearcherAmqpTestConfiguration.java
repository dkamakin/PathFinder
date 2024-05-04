package com.github.pathfinder.configuration;

import static org.mockito.Mockito.mock;
import com.github.pathfinder.service.impl.ChunkGetterService;
import com.github.pathfinder.service.impl.ChunkUpdaterService;
import com.github.pathfinder.service.impl.PointConnector;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SearcherAmqpTestConfiguration {

    @Bean
    public PointConnector pointConnector() {
        return mock(PointConnector.class);
    }

    @Bean
    public ChunkUpdaterService chunkUpdaterService() {
        return mock(ChunkUpdaterService.class);
    }

    @Bean
    public ChunkGetterService chunkGetterService() {
        return mock(ChunkGetterService.class);
    }

}
