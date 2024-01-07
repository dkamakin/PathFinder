package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.repository.PointSearcherRepository;
import com.github.pathfinder.searcher.api.exception.PointNotFoundException;
import com.github.pathfinder.service.IPointSearcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class PointSearcherService implements IPointSearcherService {

    private final PointSearcherRepository searcherRepository;
    private final CoordinateConfiguration coordinateConfiguration;

    @Override
    @ReadTransactional
    @Logged("coordinate")
    public PointNode findNearest(Coordinate coordinate) {
        var accuracy = accuracy();

        log.info("Searching the nearest point to {}, accuracy: {}", coordinate, accuracy);

        return searcherRepository
                .findNearest(coordinate.latitude(), coordinate.longitude(), accuracy)
                .orElseThrow(() -> new PointNotFoundException(coordinate));
    }

    private Double accuracy() {
        return coordinateConfiguration.getDistanceAccuracyMeters();
    }

}
