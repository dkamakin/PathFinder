package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.database.repository.PointConnectionRepository;
import com.github.pathfinder.service.IChunkService;
import com.github.pathfinder.service.IPointConnector;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PointConnector implements IPointConnector {

    private final PointConnectionRepository pointConnectionRepository;
    private final CoordinateConfiguration   coordinateConfiguration;
    private final IChunkService             chunkService;

    @Logged
    @Override
    @Transactional
    public void createConnections(List<Integer> chunkIds) {
        chunkService.markConnected(chunkIds, true);
        pointConnectionRepository.createConnections(coordinateConfiguration.getDistanceAccuracyMeters(), chunkIds);
    }

}
