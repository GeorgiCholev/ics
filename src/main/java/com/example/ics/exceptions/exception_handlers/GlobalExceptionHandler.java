package com.example.ics.exceptions.exception_handlers;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.ExceptionMessageDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidImageUrlException.class,
            ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class
    })
    protected ResponseEntity<ExceptionMessageDto> handleBadRequestException(Exception exc) {
        String message = ExceptionMessage.NOT_ACCEPTED_REQUEST;

        if (exc instanceof InvalidImageUrlException) {
            message = exc.getMessage();
        }

        return new ResponseEntity<>(new ExceptionMessageDto(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MishandledApiCallException.class)
    protected ResponseEntity<ExceptionMessageDto> handleMishandledOperationException(MishandledApiCallException exc) {
        return new ResponseEntity<>(new ExceptionMessageDto(exc.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    protected ResponseEntity<ExceptionMessageDto> handleIrretrievableDataException(ImageNotFoundException exc) {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleAllExceptions(Exception ex) {
//        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
