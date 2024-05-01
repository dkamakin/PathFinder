package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.exception.InternalServerException;
import com.github.pathfinder.database.repository.IPointConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointConnector {

    private final IPointConnectionRepository pointConnectionRepository;
    private final CoordinateConfiguration    coordinateConfiguration;
    private final ChunkUpdaterService        chunkService;

    @Logged
    @Transactional
    public void createConnections(Integer chunkId) {
        chunkService.markConnected(chunkId, true);
        var pointsInChunkConnectedStatistics = pointConnectionRepository
                .connectChunkPoints(chunkId, coordinateConfiguration.getDistanceAccuracyMeters())
                .orElseThrow(() -> new InternalServerException("Failed to connect points inside the chunk"));

        log.info("Points for the chunk {} has been connected: {}", chunkId, pointsInChunkConnectedStatistics);
    }

}
