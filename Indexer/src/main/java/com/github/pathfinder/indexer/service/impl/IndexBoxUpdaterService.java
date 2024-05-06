package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.database.repository.IndexBoxUpdaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IndexBoxUpdaterService {

    private final IndexBoxUpdaterRepository updaterRepository;

    @Logged
    @Transactional
    public IndexBoxEntity save(IndexBoxEntity entity) {
        return updaterRepository.save(entity);
    }

}
