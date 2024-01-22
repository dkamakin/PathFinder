package com.github.pathfinder.security.api.exception;

import com.github.pathfinder.core.exception.ForbiddenException;
import java.io.Serial;

public class InvalidCredentialsException extends ForbiddenException {

    @Serial
    private static final long serialVersionUID = -3812610454198828515L;

    public InvalidCredentialsException() {
        super("Credentials are not valid");
    }

}
