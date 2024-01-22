package com.github.pathfinder.core.exception;

import java.io.Serial;

public class ConflictException extends ServiceException {

    @Serial
    private static final long serialVersionUID = -4492118775838225202L;

    public ConflictException(String message, Object... parameters) {
        super(ErrorCode.CONFLICT, message, parameters);
    }

}
