package com.github.pathfinder.indexer.configuration.osm;

import com.github.pathfinder.core.data.Coordinate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexBox {

    @Valid
    @NotNull
    private IndexBox.BoxCoordinate min;

    @Valid
    @NotNull
    private IndexBox.BoxCoordinate max;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoxCoordinate {

        @NotNull
        @DecimalMin(Coordinate.Constraint.LATITUDE_MIN_VALUE_STRING)
        @DecimalMax(Coordinate.Constraint.LATITUDE_MAX_VALUE_STRING)
        private Double latitude;

        @NotNull
        @DecimalMin(Coordinate.Constraint.LONGITUDE_MIN_VALUE_STRING)
        @DecimalMax(Coordinate.Constraint.LONGITUDE_MAX_VALUE_STRING)
        private Double longitude;

    }

}
