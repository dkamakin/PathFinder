package com.github.pathfinder.service;

import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.entity.PointEntity;
import java.util.Optional;

public interface IPointService {

    PointEntity save(Point point);

    Optional<PointEntity> findNearest(Coordinate coordinate);

}
