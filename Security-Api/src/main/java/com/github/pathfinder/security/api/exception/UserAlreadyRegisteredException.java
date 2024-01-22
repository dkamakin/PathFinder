package com.github.pathfinder.security.api.exception;

import com.github.pathfinder.core.exception.ConflictException;
import java.io.Serial;

public class UserAlreadyRegisteredException extends ConflictException {

    @Serial
    private static final long serialVersionUID = 2903815946625219406L;

    public UserAlreadyRegisteredException(String username) {
        super("User '%s' is already registered", username);
    }

}
