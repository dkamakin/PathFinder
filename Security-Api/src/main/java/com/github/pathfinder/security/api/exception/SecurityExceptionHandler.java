package com.github.pathfinder.security.api.exception;

import com.github.pathfinder.core.exception.ErrorMessage;
import com.github.pathfinder.core.web.exception.tools.ExceptionHandlerTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class SecurityExceptionHandler {

    private final ExceptionHandlerTools exceptionHandlerTools;

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorMessage handle(AccessDeniedException exception) {
        return exceptionHandlerTools.message(exception);
    }

}
