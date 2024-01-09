package com.github.pathfinder.indexer.configuration;

import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.searcher.api.configuration.SearcherApiConfiguration;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

@Data
@Slf4j
@Validated
@RefreshScope
@Configuration
@EnableScheduling
@Import({CoreConfiguration.class, SearcherApiConfiguration.class})
public class IndexerConfiguration {

    public static final String INDEX_DELAY = "${index.delay}";

    @NotNull
    @Value("${index.retry.chunk.save:PT5M}")
    private Duration retryChunkSaveDelay;

    @NotNull
    @Value("${index.retry.chunk.connection:PT1H}")
    private Duration retryChunkConnectDelay;

}
