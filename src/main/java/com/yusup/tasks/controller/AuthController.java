package com.yusup.tasks.controller;

import com.yusup.tasks.model.User;
import com.yusup.tasks.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(encoder.encode(request.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok("User " + request.getUsername() + " registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequest request) {
        return userRepository.findByUsername(request.getUsername())
            .map(user -> {
                if (encoder.matches(request.getPassword(), user.getPasswordHash())) {
                    return ResponseEntity.ok("Login successful for " + request.getUsername());
                } else {
                    return ResponseEntity.status(401).body("Invalid password");
                }
            })
            .orElse(ResponseEntity.status(404).body("User not found"));
    }


    // request body object
    public static class UserRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
