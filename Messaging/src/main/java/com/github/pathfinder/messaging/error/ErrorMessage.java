package com.github.pathfinder.messaging.error;

import com.github.pathfinder.core.exception.ErrorCode;

public record ErrorMessage(ErrorCode errorCode, String message) {

}
