package com.contactsservice.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleContactNotFoundException(ContactNotFoundException ex) {
        return createResponseEntity(HttpStatus.NOT_FOUND.value(), ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ExceptionMessage> handleServiceUnavailableException(ServiceUnavailableException ex) {
        return createResponseEntity(HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ContactAlreadyExistsException.class)
    public ResponseEntity<ExceptionMessage> handleContactAlreadyExistsException(ContactAlreadyExistsException ex) {
        return createResponseEntity(HttpStatus.CONFLICT.value(), ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ContactEventCreationException.class)
    public ResponseEntity<ExceptionMessage> handleContactEventCreationException(ContactEventCreationException ex) {
        log.error("Error creating contact event", ex);
        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionMessage> createResponseEntity(int errorCode, String message, HttpStatus status) {
        ExceptionMessage exceptionMessage = new ExceptionMessage(
                errorCode,
                message,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(exceptionMessage, status);
    }
}