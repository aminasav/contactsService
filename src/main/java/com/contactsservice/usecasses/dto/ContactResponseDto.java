package com.contactsservice.usecasses.dto;

public record ContactResponseDto(
        Long cvId,
        String phoneCode,
        String phoneNumber) {
}
