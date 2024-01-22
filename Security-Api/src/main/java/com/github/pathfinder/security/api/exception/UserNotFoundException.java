package com.github.pathfinder.security.api.exception;

import com.github.pathfinder.core.exception.ForbiddenException;
import java.io.Serial;

public class UserNotFoundException extends ForbiddenException {

    @Serial
    private static final long serialVersionUID = 9191180580642397238L;

    public UserNotFoundException(String username) {
        super("User '%s' not found", username);
    }

}
