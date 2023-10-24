package com.github.pathfinder.service;

import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.database.node.PointNode;

public interface IPointSearcherService {

    PointNode findNearest(Coordinate coordinate);

}
