package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.data.path.FindPathRequest;
import com.github.pathfinder.service.IPathSearcher;
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
    private final IPathSearcher         pathSearcher;

    @Override
    @Logged("request")
    @ReadTransactional
    public AStarResult find(FindPathRequest request) {
        var source = pointSearcher.findNearest(request.source());
        var target = pointSearcher.findNearest(request.target());

        return pathSearcher.aStar(source, target);
    }

}
