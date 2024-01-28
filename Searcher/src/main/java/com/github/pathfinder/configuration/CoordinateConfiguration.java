package com.github.pathfinder.configuration;

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
public class CoordinateConfiguration {

    @Positive
    @Value("${coordinate.distance.accuracyMeters:45}")
    private double distanceAccuracyMeters;

    @Positive
    @Value("${coordinate.distance.pathAccuracyMeters:2000}")
    private double pathAccuracyMeters;

}
