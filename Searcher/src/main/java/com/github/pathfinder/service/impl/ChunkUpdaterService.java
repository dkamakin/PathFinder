package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.repository.ChunkUpdaterRepository;
import com.github.pathfinder.service.IChunkUpdaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChunkUpdaterService implements IChunkUpdaterService {

    private final ChunkUpdaterRepository updaterRepository;

    @Override
    @Transactional
    @Logged(value = "chunkNode", ignoreReturnValue = false)
    public ChunkNode save(ChunkNode node) {
        return updaterRepository.save(node);
    }

    @Override
    @Transactional
    @Logged(value = {"id", "connected"})
    public void markConnected(Integer id, boolean connected) {
        updaterRepository.markConnected(id, connected);
    }

}
