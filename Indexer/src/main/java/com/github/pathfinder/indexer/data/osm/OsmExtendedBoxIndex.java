package com.github.pathfinder.indexer.data.osm;

import com.google.common.base.MoreObjects;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public record OsmExtendedBoxIndex(List<OsmExtendedNode> nodes, Map<Long, List<OsmWay>> reverseWayIndex) {

    public List<OsmWay> ways(OsmExtendedNode node) {
        return reverseWayIndex.getOrDefault(node.node().id(), Collections.emptyList());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("nodes", nodes.size())
                .add("reverseWayIndex", reverseWayIndex.size())
                .toString();
    }

}
