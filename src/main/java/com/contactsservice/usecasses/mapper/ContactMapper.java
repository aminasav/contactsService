package com.contactsservice.usecasses.mapper;

import com.contactsservice.persistance.model.Contact;
import com.contactsservice.usecasses.client.PhoneCodeServiceFeignClient;
import com.contactsservice.usecasses.dto.ContactRequestDto;
import com.contactsservice.usecasses.dto.ContactResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public abstract class ContactMapper {

    @Autowired
    protected PhoneCodeServiceFeignClient phoneCodeServiceClient;

    @Mapping(target = "phoneCode", expression = "java(phoneCodeServiceClient.getPhoneCodeInfo(contact.getPhoneCodeId().toString()).code())")
    public abstract ContactResponseDto toResponseDto(Contact contact);

    @Mapping(target = "cvId", ignore = true)
    public abstract Contact toEntity(ContactRequestDto contactRequestDto);

    public abstract List<ContactResponseDto> toResponseDtoList(List<Contact> contacts);
}
