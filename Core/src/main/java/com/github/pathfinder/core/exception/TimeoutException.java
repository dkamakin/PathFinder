package com.github.pathfinder.core.exception;

import java.io.Serial;

public class TimeoutException extends ServiceException {

    @Serial
    private static final long serialVersionUID = -8182038150400134709L;

    public TimeoutException() {
        super(ErrorCode.TIMEOUT, "Failed to get a result within the timeout");
    }
}
