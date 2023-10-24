package com.github.pathfinder.service;

import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.entity.PointEntity;

public interface IPointService {

    PointEntity save(Point point);

    PointEntity save(PointEntity point);

}
