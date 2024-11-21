package com.contactsservice.usecasses.impl;

import com.contactsservice.persistance.model.Contact;
import com.contactsservice.persistance.repository.ContactRepository;
import com.contactsservice.usecasses.ContactService;
import com.contactsservice.usecasses.client.PhoneCodeServiceFeignClient;
import com.contactsservice.usecasses.dto.ContactRequestDto;
import com.contactsservice.usecasses.dto.ContactResponseDto;
import com.contactsservice.usecasses.dto.PhoneCodeResponseDto;
import com.contactsservice.usecasses.mapper.ContactMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    //private final PhoneCodeServiceClient phoneCodeServiceClient;
    private final PhoneCodeServiceFeignClient phoneCodeServiceClient;
    private final ContactMapper contactMapper;

    @Override
    public List<ContactResponseDto> getContacts() {
        List<Contact> contacts = contactRepository.findAll();
        return contactMapper.toResponseDtoList(contacts);
    }

    @Override
    public ContactResponseDto saveContact(ContactRequestDto contactRequestDto) {
        try {
            PhoneCodeResponseDto phoneCodeInfo = phoneCodeServiceClient.getPhoneCodeInfo(contactRequestDto.phoneCodeId().toString());
            if (phoneCodeInfo == null) {
                throw new RuntimeException("Phone code not found: " + contactRequestDto.phoneCodeId());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error while verifying phone code: " + contactRequestDto.phoneCodeId(), e);
        }

        Contact contact = contactMapper.toEntity(contactRequestDto);
        Contact savedContact = contactRepository.save(contact);
        return contactMapper.toResponseDto(savedContact);
    }
}