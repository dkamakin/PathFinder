package com.github.pathfinder.indexer.data.osm;

import java.util.Arrays;
import java.util.Optional;

public enum OsmLandType {
    SAND(3),
    BEACH(3),
    COASTLINE(3),
    WETLAND(-1),
    WATER(-1),
    BAY(-1),
    WOOD(5),
    GRASSLAND(1),
    HEATH(1),
    SCRUB(2),
    BARE_ROCK(6),
    FELL(1.5),
    PENINSULA(1),
    GLACIER(20),
    CREVASSE(100),
    DUNE(7),
    GULLY(10),
    SCREE(5),
    STONES(15),
    STONE(15),
    MOOR(2),
    TUNDRA(5),
    MUD(30);

    final double coefficient;

    OsmLandType(double coefficient) {
        this.coefficient = coefficient;
    }

    public double coefficient() {
        return coefficient;
    }

    public static Optional<OsmLandType> from(String type) {
        String upperCaseType = type.toUpperCase();

        return Arrays.stream(values())
                .filter(value -> value.name().equals(upperCaseType))
                .findFirst();
    }

}
