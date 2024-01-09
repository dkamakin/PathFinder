package com.github.pathfinder.searcher.api.exception;

import com.github.pathfinder.core.exception.InternalServerException;
import java.io.Serial;

public class ValueNotFoundException extends InternalServerException {

    @Serial
    private static final long serialVersionUID = -1889203751066861032L;

    public ValueNotFoundException(String name) {
        super("Value '%s' not found in the query response", name);
    }

}
