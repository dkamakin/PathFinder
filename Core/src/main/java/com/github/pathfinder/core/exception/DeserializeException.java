package com.github.pathfinder.core.exception;

import java.io.Serial;

public class DeserializeException extends InternalServerException {

    @Serial
    private static final long serialVersionUID = -4577103851507491818L;

    public DeserializeException(Throwable e) {
        super("Failed to deserialize: %s", e.getMessage());
    }

}
