package com.github.pathfinder.indexer.exception;

import com.github.pathfinder.core.exception.InternalServerException;
import java.io.Serial;

public class ApiExecutionException extends InternalServerException {

    @Serial
    private static final long serialVersionUID = 1339634682051737700L;

    public ApiExecutionException(String message, Object... parameters) {
        super(message, parameters);
    }

}
