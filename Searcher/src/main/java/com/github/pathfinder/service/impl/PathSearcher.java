package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.database.entity.PointEntity;
import com.github.pathfinder.database.repository.IPathRepository;
import com.github.pathfinder.service.IPathSearcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PathSearcher implements IPathSearcher {

    private final IPathRepository searcherRepository;

    @Logged
    @Override
    @ReadTransactional
    public AStarResult aStar(String graphName, PointEntity source, PointEntity target) {
        return searcherRepository.aStar(graphName, source.getId(), target.getId());
    }

}
