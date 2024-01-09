package com.github.pathfinder.core.exception;

import java.io.Serial;

public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4656699544121088883L;

    private final ErrorCode errorCode;

    public ServiceException(ErrorCode errorCode, String message, Object... parameters) {
        super(message.formatted(parameters));

        this.errorCode = errorCode;
    }

    public ErrorCode errorCode() {
        return errorCode;
    }
}
