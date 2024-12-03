package com.contactsservice.api.exception;

public class GeneralServiceException extends RuntimeException {
    public GeneralServiceException(String message) {
        super(message);
    }
}
