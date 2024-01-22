package com.github.pathfinder.data.path;

import com.github.pathfinder.database.mapper.CollectionType;
import com.github.pathfinder.database.node.PointNode;
import com.google.common.base.MoreObjects;
import java.util.List;

public record AStarResult(@CollectionType(PointNode.class)
                          List<PointNode> path,
                          Double weight,
                          Double meters) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("path", path.size())
                .add("weight", weight)
                .add("meters", meters)
                .toString();
    }

}
