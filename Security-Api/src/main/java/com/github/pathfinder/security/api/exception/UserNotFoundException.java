package com.github.pathfinder.security.api.exception;

import com.github.pathfinder.core.exception.ForbiddenException;

public class UserNotFoundException extends ForbiddenException {

    public UserNotFoundException(String username) {
        super("User '%s' not found", username);
    }

}
