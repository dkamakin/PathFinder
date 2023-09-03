package com.github.pathfinder.core.exception;

public class SerializeException extends InternalServerException {

    public SerializeException(Throwable e) {
        super(ErrorReason.SERIALIZE_ERROR.name(), "Failed to serialize: %s", e.getMessage());
    }
}
