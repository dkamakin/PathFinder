package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.data.HealthType;
import com.github.pathfinder.data.path.FindPathRequest;
import com.github.pathfinder.data.path.FindPathResponse;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.service.IPathService;
import com.github.pathfinder.service.IPointSearcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PathService implements IPathService {

    private final IPointSearcherService pointSearcher;

    @Override
    @Logged("request")
    @ReadTransactional
    public FindPathResponse find(FindPathRequest request) {
        var source = pointSearcher.findNearest(request.source());
        var target = pointSearcher.findNearest(request.target());

        return findNearest(source, target, request.health());
    }

    private FindPathResponse findNearest(PointNode source, PointNode target, HealthType health) {
        return new FindPathResponse(0);
    }

}
