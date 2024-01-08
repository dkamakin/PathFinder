package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.data.path.FindPathRequest;
import com.github.pathfinder.service.IDefaultProjectionService;
import com.github.pathfinder.service.IPathSearcher;
import com.github.pathfinder.service.IPathService;
import com.github.pathfinder.service.IPointSearcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PathService implements IPathService {

    private final IPointSearcherService     pointSearcher;
    private final IDefaultProjectionService projectionService;
    private final IPathSearcher             pathSearcher;

    @Override
    @Transactional
    @Logged("request")
    public AStarResult find(FindPathRequest request) {
        return find(request, graphName());
    }

    private AStarResult find(FindPathRequest request, String graphName) {
        var source = pointSearcher.findNearest(request.source());
        var target = pointSearcher.findNearest(request.target());

        return pathSearcher.aStar(graphName, source, target);
    }

    private String graphName() {
        return projectionService.defaultGraphName().orElseGet(projectionService::createDefaultProjection);
    }

}
