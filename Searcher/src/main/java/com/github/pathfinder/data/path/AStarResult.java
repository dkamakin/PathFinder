package com.github.pathfinder.data.path;

import com.github.pathfinder.database.mapper.CollectionType;
import com.github.pathfinder.database.node.PointNode;
import java.util.List;

public record AStarResult(@CollectionType(PointNode.class)
                          List<PointNode> path,
                          Double meters) {

}
