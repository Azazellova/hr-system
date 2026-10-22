package ru.hr.backend.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(
            MethodArgumentNotValidException exception
    ) {

        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.putIfAbsent(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        exception.getBindingResult()
                .getGlobalErrors()
                .forEach(error ->
                        errors.putIfAbsent(
                                "employee",
                                error.getDefaultMessage()
                        )
                );

        return response(
                400,
                "Validation failed",
                errors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleConstraintViolation(
            ConstraintViolationException exception
    ) {

        Map<String, String> errors = new LinkedHashMap<>();

        exception.getConstraintViolations()
                .forEach(violation ->
                        errors.put(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                );

        return response(
                400,
                "Validation failed",
                errors
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(
            ResourceNotFoundException exception
    ) {

        return response(
                404,
                "Resource not found",
                exception.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(
            IllegalArgumentException exception
    ) {

        return response(
                400,
                "Invalid request",
                exception.getMessage()
        );
    }

    private Map<String, Object> response(
            int status,
            String error,
            Object details
    ) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", status);
        response.put("error", error);
        response.put("details", details);

        return response;
    }
}