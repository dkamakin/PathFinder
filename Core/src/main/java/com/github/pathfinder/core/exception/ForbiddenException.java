package com.github.pathfinder.core.exception;

import java.io.Serial;

public class ForbiddenException extends ServiceException {

    @Serial
    private static final long serialVersionUID = -2741877494560725653L;

    public ForbiddenException(String message, Object... parameters) {
        super(ErrorCode.FORBIDDEN, message, parameters);
    }

}
