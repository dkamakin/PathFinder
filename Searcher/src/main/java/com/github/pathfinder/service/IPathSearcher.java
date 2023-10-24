package com.github.pathfinder.service;

import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.database.entity.PointEntity;

public interface IPathSearcher {

    AStarResult aStar(String graphName, PointEntity source, PointEntity target);

}
