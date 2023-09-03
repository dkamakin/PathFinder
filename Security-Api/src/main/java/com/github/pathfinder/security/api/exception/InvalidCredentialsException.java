package com.github.pathfinder.security.api.exception;

import com.github.pathfinder.core.exception.ForbiddenException;

public class InvalidCredentialsException extends ForbiddenException {

    public InvalidCredentialsException() {
        super(ErrorReason.INVALID_CREDENTIALS.name(), "Credentials are not valid");
    }
}
