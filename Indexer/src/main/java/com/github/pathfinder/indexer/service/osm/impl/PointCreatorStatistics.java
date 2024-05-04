package com.github.pathfinder.indexer.service.osm.impl;

import lombok.Data;

@Data
public class PointCreatorStatistics {

    private int unknownElevationNodes;

    public void addUnknownElevationNode() {
        unknownElevationNodes++;
    }

}
