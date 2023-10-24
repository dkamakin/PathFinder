package com.github.pathfinder.data.path;

import com.github.pathfinder.database.entity.PointEntity;
import com.github.pathfinder.database.mapper.CollectionType;
import java.util.List;

public record AStarResult(@CollectionType(PointEntity.class)
                          List<PointEntity> path,
                          Double totalCost) {

}
