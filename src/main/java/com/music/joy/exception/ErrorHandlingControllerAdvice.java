package com.music.joy.exception;

import com.music.joy.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The main purpose of this class is to handle the error flow.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RestController
@Slf4j
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse onConstraintValidationException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        log.error("MethodArgumentNotValidException found ", exception);
        return processFieldErrors(fieldErrors);
    }

    @ExceptionHandler(ArtistNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse onLongUrlNotFoundException(ArtistNotFoundException exception) {
        ErrorResponse error = ErrorResponse.builder().status(HttpStatus.NOT_FOUND).debugMessage("Long url not found").build();
        log.error("ArtistNotFoundException found ", exception);
        return error;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse onUncaughtRuntimeException(Throwable exception) {
        ErrorResponse error = ErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).debugMessage(exception.getMessage()).build();
        log.error("RuntimeException found", exception);
        return error;
    }

    private ErrorResponse processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "validation error");
        for (org.springframework.validation.FieldError fieldError : fieldErrors) {
            error.addViolationErrors(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return error;
    }
}
