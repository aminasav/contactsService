package com.contactsservice.usecasses;

import com.contactsservice.usecasses.dto.ContactRequestDto;
import com.contactsservice.usecasses.dto.ContactResponseDto;

import java.util.List;

public interface ContactService {
    List<ContactResponseDto> getContacts();
    ContactResponseDto saveContact(ContactRequestDto contactRequestDto);
}
