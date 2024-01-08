package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.service.IDefaultProjectionService;
import com.github.pathfinder.service.IProjectionService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultProjectionService implements IDefaultProjectionService {

    private static final String DEFAULT_GRAPH = "pathfinder-default";

    private final IProjectionService projectionService;

    @Logged
    @Override
    @ReadTransactional
    public Optional<String> defaultGraphName() {
        return Optional.of(DEFAULT_GRAPH).filter(projectionService::exists);
    }

    @Logged
    @Override
    @Transactional
    public String createDefaultProjection() {
        var projection = DEFAULT_GRAPH;
        var created    = projectionService.createProjection(projection);

        log.info("Create default projection result: {}", created);

        return projection;
    }

    @Logged
    @Override
    @Transactional
    public String recreateDefaultProjection() {
        projectionService.deleteAll();
        return createDefaultProjection(); // NOSONAR
    }

}
