package com.github.pathfinder.indexer.data.osm;

import java.util.List;

public record OsmWay(List<Long> nodeIds, OsmTags tags) implements OsmElement {

    @Override
    public OsmElementType type() {
        return OsmElementType.WAY;
    }

    @Override
    public OsmWay asWay() {
        return this;
    }

}
