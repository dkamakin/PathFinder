package com.github.pathfinder.indexer.configuration.elevation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@RefreshScope
@Configuration
public class OpenElevationConfiguration {

    @NotBlank
    @Value("${elevation.client.uri}")
    private String uri;

    @Positive
    @Value("${elevation.client.batchSize:20000}")
    private int batchSize;

}
