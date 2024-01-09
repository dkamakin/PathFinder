package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.repository.ChunkRepository;
import com.github.pathfinder.service.IChunkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChunkService implements IChunkService {

    private final ChunkRepository chunkRepository;

    @Override
    @Transactional
    @Logged(value = "chunkNode", ignoreReturnValue = false)
    public ChunkNode save(ChunkNode node) {
        return chunkRepository.save(node);
    }

    @Override
    @Transactional
    @Logged(value = "chunkNode", ignoreReturnValue = false)
    public List<ChunkNode> saveAll(List<ChunkNode> nodes) {
        return chunkRepository.saveAll(nodes);
    }

    @Override
    @ReadTransactional
    @Logged(value = "ids", ignoreReturnValue = false)
    public List<ChunkNode> chunks(List<Integer> ids) {
        return chunkRepository.find(ids);
    }

    @Override
    @ReadTransactional
    @Logged(value = "ids", ignoreReturnValue = false)
    public List<ChunkNode> extendedChunks(List<Integer> ids) {
        return chunkRepository.findChunkNodeByIdIn(ids);
    }

    @Override
    @Transactional
    @Logged(value = {"ids", "connected"})
    public void markConnected(List<Integer> ids, boolean connected) {
        chunkRepository.markConnected(ids, connected);
    }

}
