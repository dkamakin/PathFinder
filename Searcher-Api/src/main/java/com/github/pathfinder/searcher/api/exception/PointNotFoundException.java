package com.github.pathfinder.searcher.api.exception;

import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.exception.NotFoundException;
import java.io.Serial;
import java.util.UUID;

public class PointNotFoundException extends NotFoundException {

    @Serial
    private static final long serialVersionUID = -908944101667655500L;

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
