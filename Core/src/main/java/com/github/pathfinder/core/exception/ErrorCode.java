package com.github.pathfinder.core.exception;

public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500),
    NOT_FOUND(404),
    BAD_REQUEST(400),
    CONFLICT(409),
    UNAUTHORIZED(401),
    FORBIDDEN(403);

    private final int httpCode;

    ErrorCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public int httpCode() {
        return httpCode;
    }
}
