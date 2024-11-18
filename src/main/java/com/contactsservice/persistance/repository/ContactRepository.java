package com.contactsservice.persistance.repository;

import com.contactsservice.persistance.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
