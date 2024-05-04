package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.data.path.FindPathRequest;
import com.github.pathfinder.database.repository.IPathRepository;
import com.github.pathfinder.searcher.api.exception.PathNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PathSearcher {

    private final IPathRepository         searcherRepository;
    private final CoordinateConfiguration coordinateConfiguration;

    @Logged("request")
    @ReadTransactional
    public AStarResult aStar(FindPathRequest request) {
        return searcherRepository
                .aStar(request.source(), request.target(), coordinateConfiguration.getPathAccuracyMeters())
                .orElseThrow(PathNotFoundException::new);
    }

}
