package com.github.pathfinder.indexer.configuration;

import jakarta.validation.constraints.Positive;
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
public class SplitterConfiguration {

    public static final String SPLITTER_DELAY = "${splitter.delay}";

    @Positive
    @Value("${splitter.elementsLimit:5000}")
    private long elementsLimit;

    @Positive
    @Value("${splitter.additionalSpaceMeters:50}")
    private double additionalSpaceMeters;

}
