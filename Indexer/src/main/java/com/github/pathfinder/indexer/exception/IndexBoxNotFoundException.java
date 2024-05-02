package com.github.pathfinder.indexer.exception;

import java.io.Serial;
import com.github.pathfinder.core.exception.InternalServerException;

public class IndexBoxNotFoundException extends InternalServerException {

    @Serial
    private static final long serialVersionUID = -2788035737220098373L;

    public IndexBoxNotFoundException(int boxId) {
        super("Index box with an id %s is not found", boxId);
    }
}
