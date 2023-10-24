package com.github.pathfinder.exception;

import com.github.pathfinder.core.exception.NotFoundException;
import com.github.pathfinder.data.Coordinate;
import java.util.UUID;

public class PointNotFoundException extends NotFoundException {


    public PointNotFoundException(String descriptor) {
        super("Point '%s' not found in the graph", descriptor);
    }

    public PointNotFoundException(Coordinate coordinate) {
        this(coordinate.toString());
    }

    public PointNotFoundException(UUID id) {
        this(id.toString());
    }
}
