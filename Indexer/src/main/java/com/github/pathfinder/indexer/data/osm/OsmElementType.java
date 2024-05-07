package com.github.pathfinder.indexer.data.osm;

import lombok.Getter;

@Getter
public enum OsmElementType {
    NODE("node"),
    WAY("way");

    private final String overpassValue;

    OsmElementType(String overpassValue) {
        this.overpassValue = overpassValue;
    }

}
