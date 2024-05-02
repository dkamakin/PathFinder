package com.github.pathfinder.service.impl;

import java.util.List;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import com.github.pathfinder.database.repository.ChunkGetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChunkGetterService {

    private final ChunkGetterRepository getterRepository;

    @Logged
    @ReadTransactional
    public List<SimpleChunk> simple(List<Integer> ids) {
        return getterRepository.findAllByIdIn(ids);
    }

    @Logged
    @ReadTransactional
    public boolean exists(int id) {
        return getterRepository.existsById(id);
    }

}
