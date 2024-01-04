package com.github.pathfinder.indexer.data.osm;

import com.github.pathfinder.core.data.Coordinate;
import java.util.Map;
import java.util.Optional;

public record OsmNode(long id, Coordinate coordinate, Map<String, String> tags) implements OsmElement {

    public Optional<String> natural() {
        return Optional.ofNullable(tags.get(OsmTags.NATURAL));
    }

    @Override
    public OsmElementType type() {
        return OsmElementType.NODE;
    }

    @Override
    public OsmNode asNode() {
        return this;
    }

}
