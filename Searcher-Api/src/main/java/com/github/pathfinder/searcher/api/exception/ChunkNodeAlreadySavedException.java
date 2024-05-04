package com.github.pathfinder.searcher.api.exception;

import java.io.Serial;
import com.github.pathfinder.core.exception.BadRequestException;

public class ChunkNodeAlreadySavedException extends BadRequestException {

    @Serial
    private static final long serialVersionUID = -2940181501635759974L;

    public ChunkNodeAlreadySavedException(int nodeId) {
        super("A node with an id: %s is already saved", nodeId);
    }

}
