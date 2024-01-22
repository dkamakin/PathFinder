package com.github.pathfinder.core.exception;

import java.io.Serial;

public class InternalServerException extends ServiceException {

    @Serial
    private static final long serialVersionUID = -8886829143286165501L;

    public InternalServerException(String message, Object... parameters) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, message, parameters);
    }

}
