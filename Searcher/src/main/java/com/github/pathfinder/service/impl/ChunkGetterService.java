package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import com.github.pathfinder.database.repository.ChunkGetterRepository;
import com.github.pathfinder.service.IChunkGetterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChunkGetterService implements IChunkGetterService {

    private final ChunkGetterRepository getterRepository;

    @Logged
    @Override
    @ReadTransactional
    public List<SimpleChunk> simple(List<Integer> ids) {
        return getterRepository.findAllByIdIn(ids);
    }

}
