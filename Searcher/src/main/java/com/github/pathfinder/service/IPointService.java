package com.github.pathfinder.service;

import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.node.PointNode;

public interface IPointService {

    PointNode save(Point point);

    PointNode save(PointNode point);

}
