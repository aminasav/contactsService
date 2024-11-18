package com.contactsservice.api.controller;

import com.contactsservice.usecasses.ContactService;
import com.contactsservice.usecasses.dto.ContactRequestDto;
import com.contactsservice.usecasses.dto.ContactResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Contacts Controller", description = "API for working with contacts")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contacts")
public class ContactApiController {
    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<List<ContactResponseDto>> getContacts() {
        return new ResponseEntity<>(contactService.getContacts(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ContactResponseDto> saveContact(@RequestBody ContactRequestDto contactDto) {
        return new ResponseEntity<>(contactService.saveContact(contactDto), HttpStatus.CREATED);
    }
}
