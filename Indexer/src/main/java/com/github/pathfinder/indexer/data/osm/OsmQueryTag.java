package com.github.pathfinder.indexer.data.osm;

import java.util.List;

public record OsmQueryTag(String name, List<String> values) {
}
