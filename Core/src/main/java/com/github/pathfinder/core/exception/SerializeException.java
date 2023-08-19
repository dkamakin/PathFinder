package com.github.pathfinder.core.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class SerializeException extends InternalServerException {

    public SerializeException(JsonProcessingException e) {
        super(ErrorReason.SERIALIZE_ERROR.name(), "Failed to serialize: %s", e.getMessage());
    }
}
