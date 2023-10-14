package com.github.pathfinder.exception;

import com.github.pathfinder.core.exception.NotFoundException;

public class ProjectionNotFoundException extends NotFoundException {

    public ProjectionNotFoundException(String graphName) {
        super("Projection %s not found", graphName);
    }

}
