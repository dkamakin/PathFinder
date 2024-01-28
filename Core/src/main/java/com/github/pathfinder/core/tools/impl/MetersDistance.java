package com.github.pathfinder.core.tools.impl;

import com.github.pathfinder.core.tools.Distance;
import org.jetbrains.annotations.NotNull;

public record MetersDistance(double meters) implements Distance {

    @Override
    public int compareTo(@NotNull Distance other) {
        return Double.compare(meters, other.meters());
    }

}
