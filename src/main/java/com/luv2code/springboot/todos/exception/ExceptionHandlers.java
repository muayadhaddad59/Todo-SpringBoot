package com.luv2code.springboot.todos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ExceptionResponses> handleException(ResponseStatusException e) {
        return buildResponseEntity(e, HttpStatus.valueOf(e.getStatusCode().value()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponses> handleException(Exception e) {
        return buildResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ExceptionResponses> buildResponseEntity(Exception exc, HttpStatus status) {
        ExceptionResponses error = new ExceptionResponses();
        error.setStatus(status.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, status);
    }
}
