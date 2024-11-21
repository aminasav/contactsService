package com.contactsservice.api.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String EUROPE_MOSCOW = "Europe/Moscow";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleNotFoundException(NotFoundException e) {
        return new ExceptionMessage(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MOSCOW)));
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleBadRequestException(BadRequestException e) {
        return new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MOSCOW)));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleInternalServerErrorException(InternalServerErrorException e) {
        return new ExceptionMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MOSCOW)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleValidationErrors(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach(constraintViolation -> constraintViolation.getPropertyPath()
                .forEach(error -> errors.put(constraintViolation.getMessage(), error.getName())));
        return new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                errors.toString(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MOSCOW)));
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ExceptionMessage handleServiceUnavailableException(ServiceUnavailableException e) {
        return new ExceptionMessage(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                e.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MOSCOW)));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleException(Exception e) {
        return new ExceptionMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MOSCOW)));
    }
}
