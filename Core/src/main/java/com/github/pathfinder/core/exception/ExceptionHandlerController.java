package com.github.pathfinder.core.exception;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log(exception);
        return exception.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(this::fieldError, this::messageError));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        log(exception);
        return entity(exception);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorMessage> handleServiceException(ServiceException exception) {
        log(exception);
        return entity(exception);
    }

    private HttpStatus status(ErrorCode code) {
        return switch (code) {
            case CONFLICT -> HttpStatus.CONFLICT;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    private ResponseEntity<ErrorMessage> entity(ServiceException exception) {
        return entity(status(exception.errorCode()), exception.reason(), exception.getMessage());
    }

    private ResponseEntity<ErrorMessage> entity(Exception exception) {
        return entity(HttpStatus.INTERNAL_SERVER_ERROR, ErrorReason.INTERNAL_SERVER_ERROR.name(),
                      exception.getMessage());
    }

    private ResponseEntity<ErrorMessage> entity(HttpStatus status, String reason, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorMessage(reason, message));
    }

    private void log(Exception exception) {
        log.error("Exception caught", exception);
    }

    private String fieldError(ObjectError error) {
        return ((FieldError) error).getField();
    }

    private String messageError(ObjectError error) {
        var message = error.getDefaultMessage();

        if (message == null) {
            message = ErrorReason.ILLEGAL_ARGUMENT.name();
        }

        return message;
    }
}
