package com.github.pathfinder.core.exception;

import java.io.Serial;

public class NotFoundException extends ServiceException {

    @Serial
    private static final long serialVersionUID = -1398870159925460038L;

    public NotFoundException(String message, Object... parameters) {
        super(ErrorCode.NOT_FOUND, message, parameters);
    }

}
