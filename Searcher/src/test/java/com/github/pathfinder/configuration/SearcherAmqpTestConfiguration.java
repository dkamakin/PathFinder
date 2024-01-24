package com.github.pathfinder.configuration;

import com.github.pathfinder.service.IChunkGetterService;
import com.github.pathfinder.service.IChunkUpdaterService;
import com.github.pathfinder.service.IPointConnector;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class SearcherAmqpTestConfiguration {

    @Bean
    public IPointConnector pointConnector() {
        return mock(IPointConnector.class);
    }

    @Bean
    public IChunkUpdaterService chunkUpdaterService() {
        return mock(IChunkUpdaterService.class);
    }

    @Bean
    public IChunkGetterService chunkGetterService() {
        return mock(IChunkGetterService.class);
    }

}
