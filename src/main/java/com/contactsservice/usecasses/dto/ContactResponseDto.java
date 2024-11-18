package com.contactsservice.usecasses.dto;

public record ContactResponseDto(
        Long cvId,
        Long phoneCodeId,
        String phoneNumber) {
}
