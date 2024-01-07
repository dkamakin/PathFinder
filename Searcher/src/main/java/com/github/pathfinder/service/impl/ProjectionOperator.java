package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.service.IProjectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProjectionOperator {

    private final IProjectionService projectionService;

    @Logged
    @Transactional
    public void replaceAll(String newGraphName) {
        projectionService.deleteAll();
        projectionService.createProjection(newGraphName);
    }

}
