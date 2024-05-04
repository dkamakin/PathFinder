package com.github.pathfinder.indexer.data.osm;

import com.github.pathfinder.indexer.data.elevation.Elevation;

public record OsmExtendedNode(Elevation elevation, OsmNode node) {
}
