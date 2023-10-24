package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.repository.PointSearcherRepository;
import com.github.pathfinder.exception.PointNotFoundException;
import com.github.pathfinder.service.IDistanceCalculator;
import com.github.pathfinder.service.IPointSearcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointSearcherService implements IPointSearcherService {

    private final PointSearcherRepository searcherRepository;
    private final CoordinateConfiguration coordinateConfiguration;
    private final IDistanceCalculator     distanceCalculator;

    @Override
    @ReadTransactional
    @Logged("coordinate")
    public PointNode findNearest(Coordinate coordinate) {
        return searcherRepository
                .findNearest(coordinate.latitude(), coordinate.longitude())
                .filter(found -> isCloseEnough(coordinate, found))
                .orElseThrow(() -> new PointNotFoundException(coordinate));
    }

    private boolean isCloseEnough(Coordinate coordinate, PointNode point) {
        var targetCoordinate = new Coordinate(point.getLongitude(), point.getLatitude());
        var distance         = distanceCalculator.distance(coordinate, targetCoordinate);
        var accuracy         = coordinateConfiguration.getDistanceAccuracyMeters();

        log.info("The distance from the coordinate to the point is {}, the accuracy is {}", distance, accuracy);

        return distance.meters() <= accuracy;
    }

}
