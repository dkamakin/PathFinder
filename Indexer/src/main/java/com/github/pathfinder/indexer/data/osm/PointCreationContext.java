package com.github.pathfinder.indexer.data.osm;

import com.github.pathfinder.indexer.service.osm.impl.PointCreatorStatistics;

public record PointCreationContext(OsmExtendedBoxIndex index, PointCreatorStatistics statistics) {
}
