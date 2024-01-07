package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.repository.PointConnectionRepository;
import com.github.pathfinder.service.IChunkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PointConnector {

    private final PointConnectionRepository pointConnectionRepository;
    private final CoordinateConfiguration   coordinateConfiguration;
    private final IChunkService             chunkService;

    @Logged
    @Transactional
    public void createConnections(List<Integer> chunkIds) {
        setConnected(chunkIds);
        pointConnectionRepository.createConnections(coordinateConfiguration.getDistanceAccuracyMeters());
    }

    private void setConnected(List<Integer> chunkIds) {
        var chunks = chunkService.chunks(chunkIds);

        chunks.forEach(ChunkNode::connected);

        chunkService.saveAll(chunks);
    }

}
