package com.github.pathfinder.service;

import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.point.IndexedPoint;
import com.github.pathfinder.data.point.Point;
import java.util.Optional;

public interface IPointService {

    IndexedPoint save(Point point);

    Optional<IndexedPoint> findNearest(Coordinate coordinate);

}
