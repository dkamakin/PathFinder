package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.repository.PointRepository;
import com.github.pathfinder.service.IPointService;
import com.github.pathfinder.service.IProjectionService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService implements IPointService {

    private final PointRepository         pointRepository;
    private final CoordinateConfiguration coordinateConfiguration;
    private final IProjectionService      projectionService;

    @Logged
    @Override
    @Transactional
    public List<PointNode> saveAll(List<PointNode> points) {
        return pointRepository.saveAll(points);
    }

    @Logged
    @Override
    @Transactional
    public void createConnections() {
        pointRepository.createConnections(coordinateConfiguration.getDistanceAccuracyMeters());
        projectionService.deleteAll();
        projectionService.createProjection(UUID.randomUUID().toString());
    }

}
