package com.github.pathfinder.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@RefreshScope
@Configuration
public class CoordinateConfiguration {

    @NotNull
    @Value("${coordinate.distance.accuracyMeters:45.0}")
    private Double distanceAccuracyMeters;

}
