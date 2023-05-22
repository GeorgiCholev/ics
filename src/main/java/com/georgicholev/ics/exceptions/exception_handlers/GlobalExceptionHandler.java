package com.example.ics.exceptions.exception_handlers;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.ExceptionMessageDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidImageUrlException.class, ConstraintViolationException.class})
    protected ResponseEntity<ExceptionMessageDto> handleBadRequestException(Exception exc) {
        String message = exc.getMessage();

        if (exc instanceof ConstraintViolationException) {
            message = ExceptionMessage.NOT_ACCEPTED_REQUEST;
        }
        return new ResponseEntity<>(new ExceptionMessageDto(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MishandledApiCallException.class)
    protected ResponseEntity<ExceptionMessageDto> handleMishandledOperationException(MishandledApiCallException exc) {
        return new ResponseEntity<>(new ExceptionMessageDto(exc.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    protected ResponseEntity<ExceptionMessageDto> handleIrretrievableDataException(ImageNotFoundException exc) {
        return new ResponseEntity<>(new ExceptionMessageDto(exc.getMessage()), HttpStatus.NOT_FOUND);
    }

}
