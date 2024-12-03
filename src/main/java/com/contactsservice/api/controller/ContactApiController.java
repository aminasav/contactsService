package com.contactsservice.api.controller;

import com.contactsservice.usecasses.ContactService;
import com.contactsservice.usecasses.dto.ContactRequestDto;
import com.contactsservice.usecasses.dto.ContactResponseDto;
import com.contactsservice.usecasses.dto.PhoneCodeResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/contacts/")
public class ContactApiController {
    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<List<ContactResponseDto>> getContacts() {
        return new ResponseEntity<>(contactService.getContacts(), HttpStatus.OK);
    }

    @GetMapping("/{phoneCode}")
    public ResponseEntity<ContactResponseDto> getContactByPhoneCodeId(@PathVariable @NotNull String phoneCode) {
        return new ResponseEntity<>(contactService.getContactByPhoneCode(phoneCode), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ContactResponseDto> createContact(@RequestBody @Valid ContactRequestDto contactRequestDto) {
        return new ResponseEntity<>(contactService.createContact(contactRequestDto), HttpStatus.CREATED);
    }
}

