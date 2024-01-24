package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.exception.InternalServerException;
import com.github.pathfinder.database.repository.IPointConnectionRepository;
import com.github.pathfinder.service.IChunkUpdaterService;
import com.github.pathfinder.service.IPointConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointConnector implements IPointConnector {

    private final IPointConnectionRepository pointConnectionRepository;
    private final CoordinateConfiguration    coordinateConfiguration;
    private final IChunkUpdaterService       chunkService;

    @Logged
    @Override
    @Transactional
    public void createConnections(Integer chunkId) {
        chunkService.markConnected(chunkId, true);
        var pointsInChunkConnectedStatistics = pointConnectionRepository
                .connectChunkPoints(chunkId, coordinateConfiguration.getDistanceAccuracyMeters())
                .orElseThrow(() -> new InternalServerException("Failed to connect points inside the chunk"));

        log.info("Points inside the chunk {} has been connected: {}", chunkId, pointsInChunkConnectedStatistics);

        var connectedBoardersStatistics = pointConnectionRepository
                .connectChunkBoarders(chunkId, coordinateConfiguration.getDistanceAccuracyMeters())
                .orElseThrow(() -> new InternalServerException("Failed to connect the chunk boarders"));

        log.info("Boarders of the chunk {} has been connected: {}", chunkId, connectedBoardersStatistics);
    }

}
