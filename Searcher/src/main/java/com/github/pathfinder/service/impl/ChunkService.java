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
    public List<ChunkNode> setConnected(List<Integer> ids) {
        var chunks = chunks(ids);

        chunks.forEach(ChunkNode::connected);

        return chunkRepository.saveAll(chunks);
    }

    @Override
    @ReadTransactional
    @Logged(value = "ids", ignoreReturnValue = false)
    public List<ChunkNode> chunks(List<Integer> ids) {
        return chunkRepository.find(ids);
    }

}
