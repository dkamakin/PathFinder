package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.database.repository.ProjectionRepository;
import com.github.pathfinder.service.IProjectionService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectionService implements IProjectionService {

    private final ProjectionRepository projectionRepository;

    @Override
    @Transactional
    @Logged("graphName")
    public boolean createProjection(String graphName) {
        if (exists(graphName)) {
            return false;
        }

        int nodesCount = projectionRepository.createProjection(graphName);

        log.info("Created a projection with {} nodes", nodesCount);

        return true;
    }

    @Override
    @Transactional
    @Logged(ignoreReturnValue = false)
    public List<String> deleteAll() {
        return projectionRepository.deleteAll(projectionRepository.all());
    }

    @Override
    @ReadTransactional
    @Logged("graphName")
    public boolean exists(String graphName) {
        return projectionRepository.exists(graphName);
    }

    @Override
    @ReadTransactional
    @Logged(ignoreReturnValue = false)
    public Optional<String> defaultGraphName() {
        return projectionRepository.all().stream().findFirst();
    }

}
