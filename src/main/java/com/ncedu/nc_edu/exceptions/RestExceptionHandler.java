package com.ncedu.nc_edu.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                "Arguments validation error: " + String.join(",", errors),
                headers,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        List<String> errors = violations.stream()
                .map(v -> {
                    Path.Node last = null;
                    for (Path.Node node : v.getPropertyPath()) {
                        last = node;
                    }
                    if (last == null) {
                        return v.getMessage();
                    }

                    return "'" + last.getName() + "' " + v.getMessage();
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                String.join(", ", errors),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({
            EntityDoesNotExistsException.class
    })
    public ResponseEntity<String> handleNotFoundException(Exception ex) {
        return new ResponseEntity<>(
                ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(RequestParseException.class)
    public ResponseEntity<String> handleRequestParseException(RequestParseException ex) {
        return new ResponseEntity<>(
                ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({
            AlreadyExistsException.class
    })
    public ResponseEntity<String> handleAlreadyExistsException(Exception ex) {
        return new ResponseEntity<>(
                ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.CONFLICT
        );
    }
}
