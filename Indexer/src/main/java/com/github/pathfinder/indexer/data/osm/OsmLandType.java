package com.github.pathfinder.indexer.data.osm;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import static java.util.Map.entry;

public record OsmLandType(String name, double coefficient) {

    private static final Map<String, Double> LAND_TYPES = Map.ofEntries(
            entry("beach", 4D),
            entry("sand", 4D),
            entry("coastline", 4D),
            entry("wetland", -1D),
            entry("water", -1D),
            entry("bay", -1D),
            entry("wood", 6D),
            entry("fell", 3.5D),
            entry("moor", 4D),
            entry("tundra", 6D),
            entry("mud", 30D),
            entry("tree_row", 6D),
            entry("tree", 6D),
            entry("shrubbery", 6D),
            entry("stone", 15D),
            entry("scree", 6D),
            entry("gully", 11D),
            entry("dune", 8D),
            entry("crevasse", 100D),
            entry("glacier", 21D),
            entry("bare_rock", 7D),
            entry("scrub", 4D),
            entry("volcano", -1D),
            entry("valley", 4D),
            entry("sinkhole", -1D),
            entry("saddle", 6D),
            entry("ridge", 6D),
            entry("peak", 6D),
            entry("hill", 4D),
            entry("cliff", 8D),
            entry("unpaved", 5D),
            entry("compacted", 4D),
            entry("dirt", 5D),
            entry("stream", -1D)
    );

    private static final List<String> LAND_TYPE_KEYS = List.of("natural", "surface", "landcover", "waterway");
    public static final  OsmLandType  UNKNOWN        = new OsmLandType("unknown", 1F);

    public static Optional<OsmLandType> from(String type) {
        return Optional.ofNullable(LAND_TYPES.get(type)).map(found -> new OsmLandType(type, found));
    }

    public static Optional<OsmLandType> from(OsmTags tags) {
        return LAND_TYPE_KEYS.stream()
                .map(tags::get)
                .flatMap(Optional::stream)
                .map(OsmLandType::from)
                .flatMap(Optional::stream)
                .findFirst();
    }

}
