package com.github.pathfinder.security.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {

    public UserNotFoundException(String username) {
        super("User with name %s not found".formatted(username));
    }

}
