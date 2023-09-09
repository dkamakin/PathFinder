package com.github.pathfinder.security.api.exception;

import com.github.pathfinder.core.exception.ForbiddenException;

public class InvalidTokenException extends ForbiddenException {

    public InvalidTokenException() {
        super("Provided token is invalid");
    }
}
