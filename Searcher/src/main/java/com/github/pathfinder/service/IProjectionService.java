package com.github.pathfinder.service;

import java.util.List;
import java.util.Optional;

public interface IProjectionService {

    boolean createProjection(String graphName);

    List<String> deleteAll();

    boolean exists(String graphName);

    Optional<String> defaultGraphName();

    String createDefaultProjection();

}
