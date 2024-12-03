package com.contactsservice.api.exception;

public class ContactEventCreationException extends RuntimeException {
    public ContactEventCreationException(String message, Throwable cause) {
        super(message);
    }
}
