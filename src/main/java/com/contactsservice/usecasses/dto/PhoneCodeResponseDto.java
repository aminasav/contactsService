package com.contactsservice.usecasses.dto;

public record PhoneCodeResponseDto(
        Long id,
        String code,
        Long countryId) {
}
