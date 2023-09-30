package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.database.entity.PointEntity;
import com.github.pathfinder.exception.PointNotFoundException;
import com.github.pathfinder.service.IDistanceCalculator;
import com.github.pathfinder.service.INearestPointSearcher;
import com.github.pathfinder.service.IPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NearestPointSearcher implements INearestPointSearcher {

    private final IPointService           pointService;
    private final CoordinateConfiguration coordinateConfiguration;
    private final IDistanceCalculator     distanceCalculator;

    @Override
    @ReadTransactional
    @Logged("coordinate")
    public PointEntity findNearest(Coordinate coordinate) {
        return pointService
                .findNearest(coordinate)
                .filter(found -> isCloseEnough(coordinate, found))
                .orElseThrow(() -> new PointNotFoundException(coordinate));
    }

    private boolean isCloseEnough(Coordinate coordinate, PointEntity entity) {
        var targetCoordinate = new Coordinate(entity.getLongitude(), entity.getLatitude());
        var distance         = distanceCalculator.distance(coordinate, targetCoordinate);
        var accuracy         = coordinateConfiguration.getDistanceAccuracyMeters();

        return distance.meters() <= accuracy;
    }

}
