package com.github.pathfinder.exception;

import com.github.pathfinder.core.exception.ServiceNotFoundException;
import com.github.pathfinder.data.Coordinate;

public class PointNotFoundException extends ServiceNotFoundException {

    public PointNotFoundException(Coordinate coordinate) {
        super("Point '%s' not found in the graph", coordinate);
    }
}
