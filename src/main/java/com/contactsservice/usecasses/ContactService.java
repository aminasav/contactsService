package com.contactsservice.usecasses;

import com.contactsservice.usecasses.dto.ContactRequestDto;
import com.contactsservice.usecasses.dto.ContactResponseDto;
import com.contactsservice.usecasses.dto.PhoneCodeResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ContactService {
    List<ContactResponseDto> getContacts();

    ContactResponseDto getContactByPhoneCode(String phoneCode);

    ContactResponseDto createContact(ContactRequestDto contactRequestDto);
}
