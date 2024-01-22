package com.github.pathfinder.security.api.exception;

import com.github.pathfinder.core.exception.ForbiddenException;
import java.io.Serial;

public class InvalidTokenException extends ForbiddenException {

    @Serial
    private static final long serialVersionUID = 1624247728842499451L;

    public InvalidTokenException() {
        super("Provided token is invalid");
    }

}
