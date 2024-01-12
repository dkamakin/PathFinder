package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.exception.InternalServerException;
import com.github.pathfinder.database.repository.IPointConnectionRepository;
import com.github.pathfinder.service.IChunkUpdaterService;
import com.github.pathfinder.service.IPointConnector;
import java.util.List;
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
    public void createConnections(List<Integer> chunkIds) {
        chunkService.markConnected(chunkIds, true);
        var connected = pointConnectionRepository
                .createConnections(chunkIds, coordinateConfiguration.getDistanceAccuracyMeters())
                .orElseThrow(() -> new InternalServerException("Failed to connect the points"));

        log.info("Connected: {}", connected);
    }

}
