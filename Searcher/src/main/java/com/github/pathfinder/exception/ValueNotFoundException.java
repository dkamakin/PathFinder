package com.github.pathfinder.exception;

import com.github.pathfinder.core.exception.InternalServerException;

public class ValueNotFoundException extends InternalServerException {

    public ValueNotFoundException(String name) {
        super("Value '%s' not found in the query response", name);
    }
}
