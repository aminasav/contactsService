package com.contactsservice.persistance.repository;

import com.contactsservice.persistance.model.Contact;
import com.fasterxml.jackson.databind.introspect.AnnotationCollector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByPhoneCodeId(Long phoneCodeId);

    Optional<Contact> findByPhoneCodeIdAndPhoneNumber(Long id, String phoneNumber);
}
