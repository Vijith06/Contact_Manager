package com.ContactManager.ContactManager.Controller;

import com.ContactManager.ContactManager.Model.Contact;
import com.ContactManager.ContactManager.Repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    // Endpoint to add a new contact
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> addContact(
            @RequestParam("name") String name,
            @RequestParam(value = "phoneNumbers", required = false) String[] phoneNumbers,
            @RequestParam("email") String email,
            @RequestParam("emergencyContact") String emergencyContact,
            @RequestParam("bloodGroup") String bloodGroup,
            @RequestParam("address") String address,
            @RequestParam("company") String company,
            @RequestParam("linkedin") String linkedin,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        Contact contact = new Contact();
        contact.setName(name);
        contact.setPhonenumbers(String.join(",", phoneNumbers));
        contact.setEmail(email);
        contact.setEmergencyContact(emergencyContact);
        contact.setBloodGroup(bloodGroup);
        contact.setAddress(address);
        contact.setCompany(company);
        contact.setLinkedin(linkedin);
        
        if (image != null && !image.isEmpty()) {
            try {
                contact.setImage(image.getBytes());
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Error uploading image");
            }
        }

        contactRepository.save(contact);
        return ResponseEntity.ok("Contact added successfully");
    }

    // Endpoint to retrieve all contacts
    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        return ResponseEntity.ok(contacts);
    }

    // Endpoint to get a contact by email
    @GetMapping("/{email}")
    public ResponseEntity<Contact> getContactByEmail(@PathVariable String email) {
        Optional<Contact> optionalContact = contactRepository.findByEmail(email);
        if (!optionalContact.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optionalContact.get());
    }

    // Endpoint to update an existing contact
    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable String id, @RequestBody Contact updatedContact) {
        Optional<Contact> existingContactOptional = contactRepository.findById(id);
        if (!existingContactOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Contact existingContact = existingContactOptional.get();
        
        // Update fields
        existingContact.setName(updatedContact.getName());
        existingContact.setPhonenumbers(updatedContact.getPhonenumbers());
        existingContact.setEmail(updatedContact.getEmail());
        existingContact.setEmergencyContact(updatedContact.getEmergencyContact());
        existingContact.setBloodGroup(updatedContact.getBloodGroup());
        existingContact.setAddress(updatedContact.getAddress());
        existingContact.setCompany(updatedContact.getCompany());
        existingContact.setLinkedin(updatedContact.getLinkedin());

        // Handle image update if present
        if (updatedContact.getImage() != null && updatedContact.getImage().length > 0) {
            existingContact.setImage(updatedContact.getImage());
        }

        contactRepository.save(existingContact);
        return ResponseEntity.ok(existingContact);
    }

    // Endpoint to delete a contact
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        Optional<Contact> existingContactOptional = contactRepository.findById(id);
        if (!existingContactOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        contactRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
