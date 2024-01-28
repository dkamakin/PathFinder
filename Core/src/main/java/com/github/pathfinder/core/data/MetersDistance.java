package com.github.pathfinder.core.data;

import org.jetbrains.annotations.NotNull;

public record MetersDistance(double meters) implements Distance {

    @Override
    public int compareTo(@NotNull Distance other) {
        return Double.compare(meters, other.meters());
    }

}
