package com.contactsservice.usecasses.dto;

import lombok.Data;

@Data
public class OutboxEventDTO {

    private String eventType;

    private String payload;
}
