package com.github.pathfinder.security.api.exception;

import com.github.pathfinder.core.exception.ForbiddenException;

public class InvalidCredentialsException extends ForbiddenException {

    public InvalidCredentialsException() {
        super("Credentials are not valid");
    }
}
