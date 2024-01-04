package com.github.pathfinder.searcher.api.exception;

import com.github.pathfinder.core.exception.NotFoundException;
import java.io.Serial;

public class PathNotFoundException extends NotFoundException {

    @Serial
    private static final long serialVersionUID = -5657946567389480700L;

    public PathNotFoundException() {
        super("Path for the provided coordinates not found");
    }

}
