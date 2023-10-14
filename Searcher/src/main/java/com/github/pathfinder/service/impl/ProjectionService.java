package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.data.database.CreateProjectionResponse;
import com.github.pathfinder.database.repository.ProjectionRepository;
import com.github.pathfinder.exception.ProjectionAlreadyExistsException;
import com.github.pathfinder.exception.ProjectionNotFoundException;
import com.github.pathfinder.service.IProjectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectionService implements IProjectionService {

    private final ProjectionRepository projectionRepository;

    @Override
    @Transactional
    @Logged("graphName")
    public CreateProjectionResponse createProjection(String graphName) {
        if (exists(graphName)) {
            throw new ProjectionAlreadyExistsException(graphName);
        }

        int nodesCount = projectionRepository.createProjection(graphName);

        return new CreateProjectionResponse(nodesCount);
    }

    @Override
    @ReadTransactional
    @Logged("graphName")
    public boolean exists(String graphName) {
        return projectionRepository.exists(graphName);
    }

    @Override
    @Transactional
    @Logged("graphName")
    public void delete(String graphName) {
        if (!exists(graphName)) {
            throw new ProjectionNotFoundException(graphName);
        }

        projectionRepository.delete(graphName);
    }

}
