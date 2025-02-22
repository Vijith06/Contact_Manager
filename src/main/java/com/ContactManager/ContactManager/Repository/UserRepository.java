package com.ContactManager.ContactManager.Repository;

import com.ContactManager.ContactManager.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
