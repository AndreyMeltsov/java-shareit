package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse constraintViolationHandle(final ConstraintViolationException e) {
        log.error("Bad request : " + e.getMessage());
        return new ValidationErrorResponse(e.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse methodArgumentNotValidHandle(final MethodArgumentNotValidException e) {
        log.error("Bad request : " + e.getMessage());
        return new ValidationErrorResponse(e.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse illegalArgumentExceptionHandle(final IllegalArgumentException e) {
        log.error("Bad request : " + e.getMessage());
        return new ValidationErrorResponse(e.getMessage());
    }

    private static class ValidationErrorResponse {
        String error;

        public ValidationErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
