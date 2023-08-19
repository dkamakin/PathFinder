package com.github.pathfinder.core.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class DeserializeException extends InternalServerException {

    public DeserializeException(JsonProcessingException e) {
        super(ErrorReason.DESERIALIZE_ERROR.name(), "Failed to deserialize: %s", e.getMessage());
    }
}
