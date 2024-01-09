package com.github.pathfinder.indexer.service.osm.impl;

import lombok.Data;

@Data
public class PointCreatorStatistics {

    private int unknownTypesNodes;
    private int unknownElevationNodes;

    public void addUnknownTypeNode() {
        unknownTypesNodes++;
    }

    public void addUnknownElevationNode() {
        unknownElevationNodes++;
    }

}
