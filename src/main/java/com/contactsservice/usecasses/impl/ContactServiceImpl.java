package com.contactsservice.usecasses.impl;

import com.contactsservice.api.exception.ContactAlreadyExistsException;
import com.contactsservice.api.exception.ContactEventCreationException;
import com.contactsservice.api.exception.ContactNotFoundException;
import com.contactsservice.persistance.model.Contact;
import com.contactsservice.persistance.model.OutboxEvent;
import com.contactsservice.persistance.repository.ContactRepository;
import com.contactsservice.persistance.repository.OutboxEventRepository;
import com.contactsservice.usecasses.ContactService;
import com.contactsservice.usecasses.client.PhoneCodeServiceFeignClient;
import com.contactsservice.usecasses.dto.ContactRequestDto;
import com.contactsservice.usecasses.dto.ContactResponseDto;
import com.contactsservice.usecasses.dto.PhoneCodeResponseDto;
import com.contactsservice.usecasses.mapper.ContactMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    //private final PhoneCodeServiceClient phoneCodeServiceClient;
    private final PhoneCodeServiceFeignClient phoneCodeServiceClient;
    private final ContactMapper contactMapper;
    private final ObjectMapper objectMapper;
    private final OutboxEventRepository outboxEventRepository;


    @Override
    public List<ContactResponseDto> getContacts() {
        List<Contact> contacts = contactRepository.findAll();
        return contactMapper.toResponseDtoList(contacts);
    }

    @Override
    public ContactResponseDto getContactByPhoneCode(String phoneCode) {
        PhoneCodeResponseDto phoneCodeResponseDto = phoneCodeServiceClient.getPhoneCodeInfo(phoneCode);

        Contact contact = contactRepository.findByPhoneCodeId(phoneCodeResponseDto.id())
                .orElseThrow(() -> new ContactNotFoundException("Contact with the specified phone code ID not found"));

        log.info("Contact successfully retrieved with id: {}", contact.getCvId());
        return contactMapper.toResponseDto(contact);
    }

    @Override
    @Transactional
    public ContactResponseDto createContact(ContactRequestDto contactRequestDto) {
        if (contactRepository.findByPhoneCodeIdAndPhoneNumber(contactRequestDto.phoneCodeId(), contactRequestDto.phoneNumber()).isPresent()) {
            log.error("Contact with phone code ID {} and phone number {} already exists", contactRequestDto.phoneCodeId(), contactRequestDto.phoneNumber());
            throw new ContactAlreadyExistsException("Contact with the given phone code ID " + contactRequestDto.phoneCodeId() + " and phone number " + contactRequestDto.phoneNumber() + " already exists");
        }

        Contact contact = contactMapper.toEntity(contactRequestDto);
        contactRepository.save(contact);

        saveOutboxEvent(contact);

        log.info("Contact successfully created with id: {}", contact.getCvId());
        return contactMapper.toResponseDto(contact);
    }

    private void saveOutboxEvent(Contact contact) {
        OutboxEvent event = new OutboxEvent();
        event.setEventType("ContactCreated");

        try {
            event.setPayload(objectMapper.writeValueAsString(contact));
        } catch (JsonProcessingException e) {
            log.error("Error processing contact {}: {}", contact.getCvId(), e.getMessage());
            throw new ContactEventCreationException("Failed to create contact event for contact with ID " + contact.getCvId(), e);
        }
        outboxEventRepository.save(event);
    }
}

