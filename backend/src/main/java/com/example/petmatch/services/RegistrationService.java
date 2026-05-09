package com.example.petmatch.services;

import com.example.petmatch.domain.dtos.RegistrationRequest;
import com.example.petmatch.domain.entities.User;

public interface RegistrationService {
    User register(RegistrationRequest request);
}
