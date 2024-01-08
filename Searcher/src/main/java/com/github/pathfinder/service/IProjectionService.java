package com.github.pathfinder.service;

import java.util.List;

public interface IProjectionService {

    boolean createProjection(String graphName);

    List<String> deleteAll();

    boolean exists(String graphName);

}
