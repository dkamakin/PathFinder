package com.github.pathfinder.exception;

import com.github.pathfinder.core.exception.BadRequestException;

public class ProjectionAlreadyExistsException extends BadRequestException {

    public ProjectionAlreadyExistsException(String graphName) {
        super("Projection '%s' already exists", graphName);
    }

}
