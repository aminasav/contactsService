package com.contactsservice.usecasses.mapper;

import com.contactsservice.persistance.model.OutboxEvent;
import com.contactsservice.usecasses.dto.OutboxEventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OutboxEventMapper {

    OutboxEventDTO toDto(OutboxEvent event);

    OutboxEvent toEntity(OutboxEventDTO dto);
}
