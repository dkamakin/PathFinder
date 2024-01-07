package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.repository.PointRepository;
import com.github.pathfinder.service.IChunkService;
import com.github.pathfinder.service.IPointService;
import com.github.pathfinder.service.IProjectionService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class PointService implements IPointService {

    private final PointRepository         pointRepository;
    private final CoordinateConfiguration coordinateConfiguration;
    private final IProjectionService      projectionService;
    private final IChunkService chunkService;

    @Override
    @Transactional
    @Logged("chunkId")
    public List<PointNode> saveAll(Integer chunkId, List<PointNode> nodes) {
        var saved = pointRepository.saveAll(nodes);

        log.info("Saved {} points", saved.size());

        chunkService.save(new ChunkNode(chunkId));

        return saved;
    }

    @Override
    @Transactional
    @Logged("chunkIds")
    public void createConnections(List<Integer> chunkIds) {
        pointRepository.createConnections(coordinateConfiguration.getDistanceAccuracyMeters());
        projectionService.deleteAll();
        projectionService.createProjection(UUID.randomUUID().toString());
        setConnected(chunkIds);
    }

    private void setConnected(List<Integer> chunkIds) {
        var chunks = chunkService.chunks(chunkIds);

        chunks.forEach(ChunkNode::connected);

        chunkService.saveAll(chunks);
    }

}
