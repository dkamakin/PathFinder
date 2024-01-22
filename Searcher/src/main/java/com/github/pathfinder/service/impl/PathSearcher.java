package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.data.path.FindPathRequest;
import com.github.pathfinder.database.repository.IPathRepository;
import com.github.pathfinder.searcher.api.exception.PathNotFoundException;
import com.github.pathfinder.service.IPathSearcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PathSearcher implements IPathSearcher {

    private final IPathRepository searcherRepository;

    @Override
    @Logged("request")
    @ReadTransactional
    public AStarResult aStar(FindPathRequest request) {
        return searcherRepository
                .aStar(request.source(), request.target())
                .orElseThrow(PathNotFoundException::new);
    }

}
