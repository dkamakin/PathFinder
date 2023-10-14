package com.github.pathfinder.service;

import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.database.entity.PointEntity;
import java.util.Optional;

public interface IPointSearcherService {

    Optional<PointEntity> findNearest(Coordinate coordinate);

}
