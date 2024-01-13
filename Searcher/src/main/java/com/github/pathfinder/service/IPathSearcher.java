package com.github.pathfinder.service;

import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.data.path.FindPathRequest;

public interface IPathSearcher {

    AStarResult aStar(FindPathRequest request);

}
