package com.github.pathfinder.indexer.data.osm;

public interface OsmElement {

    OsmElementType type();

    OsmTags tags();

    default OsmNode asNode() {
        throw new IllegalArgumentException("The object is not a node");
    }

    default OsmWay asWay() {
        throw new IllegalArgumentException("The object is not a way");
    }

}
