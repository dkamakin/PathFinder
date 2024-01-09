package com.github.pathfinder.indexer.database.entity;

import com.github.pathfinder.core.data.Coordinate;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MaxBoxCoordinate {

    @UtilityClass
    public static final class Token {

        public static final String MAX_LATITUDE  = "MAX_LATITUDE";
        public static final String MAX_LONGITUDE = "MAX_LONGITUDE";
    }

    @Column(name = Token.MAX_LATITUDE)
    @DecimalMin(Coordinate.Constraint.LATITUDE_MIN_VALUE_STRING)
    @DecimalMax(Coordinate.Constraint.LATITUDE_MAX_VALUE_STRING)
    private double latitude;

    @Column(name = Token.MAX_LONGITUDE)
    @DecimalMin(Coordinate.Constraint.LONGITUDE_MIN_VALUE_STRING)
    @DecimalMax(Coordinate.Constraint.LONGITUDE_MAX_VALUE_STRING)
    private double longitude;

}
