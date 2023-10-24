package com.github.pathfinder.service;

import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.database.node.PointNode;

public interface IPathSearcher {

    AStarResult aStar(String graphName, PointNode source, PointNode target);

}
