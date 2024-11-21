package com.contactsservice.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionMessage {
    private int errorCode;
    private String message;
    private ZonedDateTime timeStamp;
}