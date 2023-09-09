package com.github.pathfinder.core.exception;

public class DeserializeException extends InternalServerException {

    public DeserializeException(Throwable e) {
        super("Failed to deserialize: %s", e.getMessage());
    }
}
