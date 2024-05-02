package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.exception.BadRequestException;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.repository.ChunkUpdaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChunkUpdaterService {

    private final ChunkUpdaterRepository updaterRepository;
    private final ChunkGetterService     getterService;

    @Transactional
    @Logged(value = "chunkNode", ignoreReturnValue = false)
    public ChunkNode save(ChunkNode node) {
        if (getterService.exists(node.getId())) {
            throw new BadRequestException("A node with an id: {} is already saved", node.getId());
        }

        return updaterRepository.save(node);
    }

    @Transactional
    @Logged(value = {"id", "connected"})
    public void markConnected(Integer id, boolean connected) {
        updaterRepository.markConnected(id, connected);
    }

}
