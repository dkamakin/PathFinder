package com.github.pathfinder.data.point;

import com.github.pathfinder.database.node.LandType;
import java.util.Set;
import lombok.Builder;

@Builder
public record Point(Double altitude,
                    Double longitude,
                    Double latitude,
                    LandType landType,
                    Set<PointConnection> connections) {

    public Point(Double altitude, Double longitude, Double latitude, LandType landType) {
        this(altitude, longitude, latitude, landType, Set.of());
    }

    public record PointConnection(Point target,
                                  Double distance) {

    }
}
