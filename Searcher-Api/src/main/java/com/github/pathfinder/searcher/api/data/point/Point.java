package com.github.pathfinder.searcher.api.data.point;

import com.github.pathfinder.core.data.Coordinate;

public record Point(Double altitude,
                    Coordinate coordinate,
                    String landType,
                    Double passabilityCoefficient) {
}
