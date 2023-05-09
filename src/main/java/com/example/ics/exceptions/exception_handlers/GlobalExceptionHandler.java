package com.example.ics.exceptions.exception_handlers;

import com.example.ics.exceptions.MishandledApiCallException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MishandledApiCallException.class})
    protected ResponseEntity<Object> handleException(Exception exception) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

}
