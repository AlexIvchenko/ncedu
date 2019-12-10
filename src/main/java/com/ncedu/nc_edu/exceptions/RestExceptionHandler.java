package com.ncedu.nc_edu.exceptions;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Map<String, String>> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(errorField ->
                errors.add(Map.of("field", errorField.getField(), "message", errorField.getDefaultMessage())));
        JSONObject body = new JSONObject();
        body.put("errors", errors);
        return new ResponseEntity<>(
                body,
                headers,
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatus status,
                                                         WebRequest request
    ) {
        JSONObject json = new JSONObject();
        json.put("error", HttpStatus.BAD_REQUEST.value());

        List<FieldError> errors = ex.getFieldErrors();
        List<String> messages = new ArrayList<>();
        for (FieldError error : errors) {
            messages.add(error.getField() + " cannot be " + error.getRejectedValue());
        }

        json.put("messages", messages);

        return new ResponseEntity<>(
                json,
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

        JSONObject json = new JSONObject();
        json.put("error", HttpStatus.BAD_REQUEST.value());
        json.put("message", String.join(", ", errors));

        return new ResponseEntity<>(
                json,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({EntityDoesNotExistsException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        JSONObject json = new JSONObject();

        json.put("error", HttpStatus.NOT_FOUND.value());
        json.put("message", ex.getMessage());
        return new ResponseEntity<>(
                json,
                new HttpHeaders(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(RequestParseException.class)
    public ResponseEntity<Object> handleRequestParseException(RequestParseException ex) {
        JSONObject json = new JSONObject();

        json.put("error", HttpStatus.BAD_REQUEST.value());
        json.put("message", ex.getMessage());
        return new ResponseEntity<>(
                json,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(Exception ex) {
        JSONObject json = new JSONObject();

        json.put("error", HttpStatus.CONFLICT.value());
        json.put("message", ex.getMessage());

        return new ResponseEntity<>(
                json,
                new HttpHeaders(),
                HttpStatus.CONFLICT
        );
    }

    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        JSONObject body = new JSONObject();
        JSONObject error = new JSONObject();

        error.put("field", ((MismatchedInputException)ex.getCause()).getPath().get(0).getFieldName());
        error.put("message", "Not a valid value");
        body.put("errors", Collections.singleton(error));

        return new ResponseEntity<>(
                body,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }
}
