package com.github.pathfinder.indexer.configuration;

import java.util.concurrent.Executors;
import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.indexer.service.impl.IndexThreadPool;
import com.github.pathfinder.searcher.api.configuration.SearcherApiConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

@Data
@Slf4j
@Validated
@EnableRetry
@RefreshScope
@Configuration
@EnableScheduling
@ConfigurationPropertiesScan
@Import({CoreConfiguration.class, SearcherApiConfiguration.class})
public class IndexerConfiguration {

    public static final  String INDEX_DELAY         = "${index.delay}";
    private static final String THREAD_NAME_PATTERN = "index-";

    @Bean
    public IndexThreadPool indexThreadPool() {
        return new IndexThreadPool(Executors.newThreadPerTaskExecutor(Thread.ofVirtual()
                                                                              .name(THREAD_NAME_PATTERN, 0)
                                                                              .factory()));

    }

}
