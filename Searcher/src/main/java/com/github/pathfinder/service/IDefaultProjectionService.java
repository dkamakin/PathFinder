package com.github.pathfinder.service;

import java.util.Optional;

public interface IDefaultProjectionService {

    Optional<String> defaultGraphName();

    String createDefaultProjection();

    String recreateDefaultProjection();

}
