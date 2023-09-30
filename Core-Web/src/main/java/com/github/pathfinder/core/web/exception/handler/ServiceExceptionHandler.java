package com.github.pathfinder.core.web.exception.handler;

import com.github.pathfinder.core.exception.ErrorMessage;
import com.github.pathfinder.core.exception.ServiceException;
import com.github.pathfinder.core.web.exception.tools.ExceptionHandlerTools;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ServiceExceptionHandler {

    private final ExceptionHandlerTools exceptionHandlerTools;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ServiceFieldError> handle(MethodArgumentNotValidException exception) {
        var result = exception.getBindingResult().getAllErrors().stream().map(this::serviceFieldError).toList();

        log.error("Bad request caught: {}", result);

        return result;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handle(HttpMessageNotReadableException exception) {
        return exceptionHandlerTools.message(exception);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorMessage> handle(ServiceException exception) {
        return ResponseEntity
                .status(exception.errorCode().httpCode())
                .body(exceptionHandlerTools.message(exception));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handle(Exception exception) {
        return exceptionHandlerTools.message(exception);
    }

    private ServiceFieldError serviceFieldError(ObjectError error) {
        return new ServiceFieldError(fieldName(error), messageError(error));
    }

    private String fieldName(ObjectError error) {
        return ((FieldError) error).getField();
    }

    private String messageError(ObjectError error) {
        var message = error.getDefaultMessage();

        if (message == null) {
            message = "Illegal argument";
        }

        return message;
    }
}
