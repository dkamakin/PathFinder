package com.github.pathfinder.core.data;

public record MetersDistance(double meters) implements Distance {

    @Override
    public int compareTo(Distance other) {
        return Double.compare(meters, other.meters());
    }

}
