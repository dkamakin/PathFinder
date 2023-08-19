package com.github.pathfinder.core.exception;

public class ServiceDuplicateException extends ServiceException {

    public ServiceDuplicateException(String reason, String message, Object... params) {
        super(ErrorCode.CONFLICT, reason, message, params);
    }
}
