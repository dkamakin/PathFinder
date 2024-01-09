package com.github.pathfinder.core.exception;

import java.io.Serial;

public class SerializeException extends InternalServerException {

    @Serial
    private static final long serialVersionUID = -3363159328861733317L;

    public SerializeException(Throwable e) {
        super("Failed to serialize: %s", e.getMessage());
    }

}
