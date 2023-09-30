package com.github.pathfinder.service;

import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.database.entity.PointEntity;

public interface INearestPointSearcher {

    PointEntity findNearest(Coordinate coordinate);

}
