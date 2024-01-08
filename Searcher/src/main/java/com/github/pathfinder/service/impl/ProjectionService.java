package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.database.repository.IProjectionRepository;
import com.github.pathfinder.service.IProjectionService;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectionService implements IProjectionService {

    private final IProjectionRepository projectionRepository;

    @Override
    @Transactional
    @Logged("graphName")
    public synchronized boolean createProjection(String graphName) {
        if (exists(graphName)) {
            return false;
        }

        var projectionStatistics = projectionRepository.createProjection(graphName);

        log.info("Created a projection {}, {}", graphName, projectionStatistics);

        return true;
    }

    @Override
    @Transactional
    @Logged(ignoreReturnValue = false)
    public List<String> deleteAll() {
        return Lists.newArrayList(projectionRepository.deleteAll(projectionRepository.all()));
    }

    @Override
    @ReadTransactional
    @Logged("graphName")
    public boolean exists(String graphName) {
        return projectionRepository.exists(graphName);
    }

}
