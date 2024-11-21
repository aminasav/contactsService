package com.contactsservice.usecasses.dto;

import jakarta.validation.constraints.NotNull;

public record ContactRequestDto(
        @NotNull
        Long phoneCodeId,
        @NotNull
        String phoneNumber) {
}
