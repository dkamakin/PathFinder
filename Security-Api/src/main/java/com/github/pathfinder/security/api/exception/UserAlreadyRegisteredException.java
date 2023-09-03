package com.github.pathfinder.security.api.exception;

import com.github.pathfinder.core.exception.ConflictException;

public class UserAlreadyRegisteredException extends ConflictException {

    public UserAlreadyRegisteredException(String username) {
        super(ErrorReason.USER_ALREADY_REGISTERED.name(), "User '%s' is already registered", username);
    }
}
