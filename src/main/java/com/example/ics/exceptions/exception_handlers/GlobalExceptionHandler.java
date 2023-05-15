package com.example.ics.exceptions.exception_handlers;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
//        TODO: Add messages

//    @ExceptionHandler({MishandledApiCallException.class, InvalidImageUrlException.class, ImageNotFoundException.class})
//    protected ResponseEntity<Object> handleException(Exception exception) {
//
//
//        if (exception instanceof InvalidImageUrlException) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } else if (exception instanceof ImageNotFoundException) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } else {
//            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
//        }
//    }

    @ExceptionHandler({InvalidImageUrlException.class, ConstraintViolationException.class})
    protected ResponseEntity<Object> handleBadRequestException(Exception exc) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MishandledApiCallException.class)
    protected ResponseEntity<Object> handleMishandledOperationException(MishandledApiCallException exc) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    protected ResponseEntity<Object> handleIrretrievableDataException(ImageNotFoundException exc) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
