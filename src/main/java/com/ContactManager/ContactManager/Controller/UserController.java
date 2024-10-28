package com.ContactManager.ContactManager.Controller;

import com.ContactManager.ContactManager.Model.*;
import com.ContactManager.ContactManager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        
        // Check if user with the same email exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            response.put("message", "Email already exists!");
            return ResponseEntity.badRequest().body(response); // Return 400 Bad Request
        }

        // Save the user to the database
        userRepository.save(user);
        response.put("message", "User created successfully!");
        return ResponseEntity.ok(response); // Return 200 OK
    }
    @PostMapping("/signin")
public ResponseEntity<Map<String, String>> signin(@RequestBody UserCredentials credentials) {
    Map<String, String> response = new HashMap<>();
    
    // Fetch the user by email
    User user = userRepository.findByEmail(credentials.getEmail());
    
    // Check if user exists and password matches
    if (user == null || !user.getPassword().equals(credentials.getPassword())) {
        response.put("message", "Invalid email or password!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // Return 401 Unauthorized
    }

    response.put("message", "Login successful!");
    return ResponseEntity.ok(response); // Return 200 OK
}

  
}
