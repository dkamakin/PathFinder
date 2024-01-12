package com.github.pathfinder.indexer.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Slf4j
@Validated
@RefreshScope
@Configuration
public class IndexerRetryConfiguration {

    @NotNull
    @Value("${index.retry.chunk.save:PT5M}")
    private Duration saveDelay;

    @NotNull
    @Value("${index.retry.chunk.connection:PT1H}")
    private Duration connectDelay;

}
