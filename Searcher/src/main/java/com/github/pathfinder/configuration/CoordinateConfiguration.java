package com.github.pathfinder.configuration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@ToString
@Getter
@RefreshScope
@Configuration
@EqualsAndHashCode
public class CoordinateConfiguration {

    @UtilityClass
    public static class Token {

        public static final String DISTANCE_ACCURACY_METERS = "${coordinate.distance.accuracyMeters}";
    }

    @Value(Token.DISTANCE_ACCURACY_METERS)
    private Double distanceAccuracyMeters;

}
