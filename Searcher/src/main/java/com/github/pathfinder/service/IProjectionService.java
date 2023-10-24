package com.github.pathfinder.service;

import com.github.pathfinder.data.database.CreateProjectionResponse;

public interface IProjectionService {

    CreateProjectionResponse createProjection(String graphName);

    boolean exists(String graphName);

    void tryDelete(String graphName);

}
