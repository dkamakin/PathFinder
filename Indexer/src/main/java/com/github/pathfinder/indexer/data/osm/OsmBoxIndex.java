package com.github.pathfinder.indexer.data.osm;

import com.google.common.base.MoreObjects;
import java.util.List;
import java.util.Map;

public record OsmBoxIndex(List<OsmNode> nodes, Map<Long, List<OsmWay>> reverseWayIndex) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("nodes", nodes.size())
                .add("reverseWayIndex", reverseWayIndex.size())
                .toString();
    }

}
