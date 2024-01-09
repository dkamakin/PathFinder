package com.github.pathfinder.core.exception;

import java.io.Serial;

public class BadRequestException extends ServiceException {

    @Serial
    private static final long serialVersionUID = 8348726943577585851L;

    public BadRequestException(String message, Object... params) {
        super(ErrorCode.BAD_REQUEST, message, params);
    }

}
