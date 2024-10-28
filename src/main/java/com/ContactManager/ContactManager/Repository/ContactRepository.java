package com.ContactManager.ContactManager.Repository;

import com.ContactManager.ContactManager.Model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends MongoRepository<Contact, String> {
    
    // Method to find a contact by email
    Optional<Contact> findByEmail(String email);

    // You can add more custom query methods here if needed
}
