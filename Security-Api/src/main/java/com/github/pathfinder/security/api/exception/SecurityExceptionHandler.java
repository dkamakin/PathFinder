package com.github.pathfinder.security.api.exception;

import com.github.pathfinder.core.exception.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SecurityExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorMessage handle(AccessDeniedException exception) {
        return new ErrorMessage(ErrorReason.INVALID_CREDENTIALS.name(), exception.getMessage());
    }

}
