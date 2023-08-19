package com.github.pathfinder.service;

import com.github.pathfinder.data.path.FindPathRequest;
import com.github.pathfinder.data.path.FindPathResponse;

public interface IPathService {

    FindPathResponse find(FindPathRequest request);

}
