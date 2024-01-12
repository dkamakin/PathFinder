package com.github.pathfinder.indexer.data.osm;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import static java.util.Map.entry;

public record OsmLandType(String name, double coefficient) {

    private static final Map<String, Double> LAND_TYPES = Map.ofEntries(
            entry("beach", 3D),
            entry("sand", 3D),
            entry("coastline", 3D),
            entry("wetland", -1D),
            entry("water", -1D),
            entry("bay", -1D),
            entry("wood", 5D),
            entry("fell", 1.5D),
            entry("grassland", 1D),
            entry("heath", 1D),
            entry("moor", 2D),
            entry("tundra", 5D),
            entry("mud", 30D),
            entry("tree_row", 5D),
            entry("tree", 5D),
            entry("shrubbery", 5D),
            entry("stone", 15D),
            entry("scree", 5D),
            entry("gully", 10D),
            entry("dune", 7D),
            entry("crevasse", 100D),
            entry("glacier", 20D),
            entry("peninsula", 1D),
            entry("bare_rock", 6D),
            entry("scrub", 2D),
            entry("volcano", -1D),
            entry("valley", 3D),
            entry("sinkhole", -1D),
            entry("saddle", 5D),
            entry("ridge", 5D),
            entry("peak", 5D),
            entry("hill", 2D),
            entry("cliff", 7D)
    );

    private static final List<String> LAND_TYPE_KEYS = List.of("natural", "surface", "landcover");
    public static final  OsmLandType  UNKNOWN        = new OsmLandType("unknown", 1F);

    public static Optional<OsmLandType> from(String type) {
        return Optional.ofNullable(LAND_TYPES.get(type)).map(found -> new OsmLandType(type, found));
    }

    public static Optional<OsmLandType> from(OsmTags tags) {
        return LAND_TYPE_KEYS.stream()
                .map(tags::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(OsmLandType::from)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

}
