package com.example.petmatch.controllers;

import com.example.petmatch.domain.dtos.RegistrationRequest;
import com.example.petmatch.domain.entities.User;
import com.example.petmatch.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        try {
            User created = registrationService.register(request);
            return ResponseEntity.status(201).body(created.getEmail());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
