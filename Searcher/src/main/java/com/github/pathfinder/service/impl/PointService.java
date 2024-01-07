package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.repository.PointRepository;
import com.github.pathfinder.service.IChunkService;
import com.github.pathfinder.service.IPointService;
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

    private final PointRepository    pointRepository;
    private final PointConnector     pointConnector;
    private final ProjectionOperator projectionOperator;
    private final IChunkService      chunkService;

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
    @Logged("chunkIds")
    public void createConnections(List<Integer> chunkIds) {
        pointConnector.createConnections(chunkIds);
        projectionOperator.replaceAll(UUID.randomUUID().toString());
    }

}
