package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.HealthType;
import com.github.pathfinder.data.path.FindPathRequest;
import com.github.pathfinder.data.path.FindPathResponse;
import com.github.pathfinder.data.point.IndexedPoint;
import com.github.pathfinder.exception.PointNotFoundException;
import com.github.pathfinder.service.IPathService;
import com.github.pathfinder.service.IPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PathService implements IPathService {

    private final IPointService pointService;

    @Override
    @Logged("request")
    @ReadTransactional
    public FindPathResponse find(FindPathRequest request) {
        var source = find(request.source());
        var target = find(request.target());

        if (isClosePoints(source, target)) {
            log.info("Found close points, no need to calculate");
            return new FindPathResponse(0);
        } else {
            return find(source, target, request.health());
        }
    }

    private FindPathResponse find(IndexedPoint source, IndexedPoint target, HealthType health) {
        return new FindPathResponse(0);
    }

    private boolean isClosePoints(IndexedPoint first, IndexedPoint second) {
        log.info("Is close points: {} and {}", first, second);
        return false;
    }

    private IndexedPoint find(Coordinate coordinate) {
        return pointService.findNearest(coordinate).orElseThrow(() -> new PointNotFoundException(coordinate));
    }

}
