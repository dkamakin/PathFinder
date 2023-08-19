package com.github.pathfinder.security.exception;

import com.github.pathfinder.core.exception.ServiceDuplicateException;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAlreadyRegisteredException extends ServiceDuplicateException {

    public UserAlreadyRegisteredException(String username) {
        super(ErrorReason.USER_ALREADY_REGISTERED.name(), "User with username %s already registered", username);
    }

    public UserAlreadyRegisteredException(UserDetails user) {
        this(user.getUsername());
    }
}
