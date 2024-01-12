package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.repository.ChunkUpdaterRepository;
import com.github.pathfinder.service.IChunkUpdaterService;
import java.util.List;
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
    @Logged(value = {"ids", "connected"})
    public void markConnected(List<Integer> ids, boolean connected) {
        updaterRepository.markConnected(ids, connected);
    }

}
