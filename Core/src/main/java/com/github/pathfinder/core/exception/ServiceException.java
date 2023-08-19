package com.github.pathfinder.core.exception;

public class ServiceException extends RuntimeException {

    private final String    reason;
    private final ErrorCode errorCode;

    public ServiceException(ErrorCode errorCode, String reason, String message, Object... params) {
        super(message.formatted(params));

        this.reason = reason;
        this.errorCode = errorCode;
    }

    public String reason() {
        return reason;
    }

    public ErrorCode errorCode() {
        return errorCode;
    }
}
