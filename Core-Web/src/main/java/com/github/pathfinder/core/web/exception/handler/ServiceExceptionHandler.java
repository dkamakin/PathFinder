package com.github.pathfinder.core.web.exception.handler;

import com.github.pathfinder.core.exception.ErrorMessage;
import com.github.pathfinder.core.exception.ServiceException;
import com.github.pathfinder.core.web.exception.tools.ExceptionHandlerTools;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ServiceExceptionHandler {

    private final ExceptionHandlerTools exceptionHandlerTools;

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            TypeMismatchException.class,
            InvalidPropertyException.class,
            ServletRequestBindingException.class,
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBadRequest(Exception exception) {
        log(exception);
        return exceptionHandlerTools.message(exception);

    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ServiceFieldError> handle(BindException bindException) {
        return fieldsErrors(bindException.getAllErrors().stream(), this::serviceFieldError);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ServiceFieldError> handle(MethodArgumentNotValidException exception) {
        return fieldsErrors(exception.getBindingResult().getAllErrors().stream(), this::serviceFieldError);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<ServiceFieldError> handle(ConstraintViolationException exception) {
        return fieldsErrors(exception.getConstraintViolations().stream(), this::serviceFieldError);
    }

    private <T> List<ServiceFieldError> fieldsErrors(Stream<T> errors, Function<T, ServiceFieldError> mapper) {
        List<ServiceFieldError> resolved = errors.map(mapper).toList();

        log.error("Exception intercepted: {}", resolved);

        return resolved;
    }

    private <T> String pathError(ConstraintViolation<T> violation) {
        return violation.getPropertyPath().toString();
    }

    private <T> String messageError(ConstraintViolation<T> violation) {
        return violation.getMessage();
    }

    private <T> ServiceFieldError serviceFieldError(ConstraintViolation<T> violation) {
        return new ServiceFieldError(pathError(violation), messageError(violation));
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

    private void log(Exception exception) {
        log.error("Exception intercepted", exception);
    }

}
