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
import java.util.List;
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
        return new ResponseEntity<Object>(
                "Arguments validation error: " + String.join(",", errors),
                headers,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({
            EntityDoesNotExistsException.class
    })
    public ResponseEntity<String> handleNotFoundException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({
            EmailAlreadyExistsException.class
    })
    public ResponseEntity<String> handleAlreadyExistsException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.CONFLICT
        );
    }
}
