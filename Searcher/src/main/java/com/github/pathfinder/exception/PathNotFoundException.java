package com.github.pathfinder.exception;

import com.github.pathfinder.core.exception.NotFoundException;

public class PathNotFoundException extends NotFoundException {

    public PathNotFoundException() {
        super("Path for the provided coordinates not found");
    }

}
