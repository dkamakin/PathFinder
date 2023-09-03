package com.github.pathfinder.security.api.exception;

import com.github.pathfinder.core.exception.ForbiddenException;

public class UserNotFoundException extends ForbiddenException {

    public UserNotFoundException() {
        super(ErrorReason.USER_NOT_FOUND.name(), "User with provided token not found");
    }

    public UserNotFoundException(String username) {
        super(ErrorReason.USER_NOT_FOUND.name(), "User '%s' not found", username);
    }

}
