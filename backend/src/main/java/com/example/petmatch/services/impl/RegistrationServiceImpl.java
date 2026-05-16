package com.example.petmatch.services.impl;

import com.example.petmatch.domain.dtos.RegistrationRequest;
import com.example.petmatch.domain.entities.User;
import com.example.petmatch.repositories.UserRepository;
import com.example.petmatch.services.RegistrationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Implementation of the RegistrationService interface for handling user registration logic.
 * This service is responsible for validating registration requests, checking for existing users,
 * encoding passwords, and saving new users to the database.
 * It uses the UserRepository to interact with the database and PasswordEncoder to securely hash user passwords.
 * The Service annotation indicates that this class is a bean managed by the Spring framework, and the
 * RequiredArgsConstructor annotation generates a constructor with required arguments (final fields) for dependency
 * injection.
 */
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    /**
     * Repository for accessing user data in the database.
     * This repository handles CRUD operations for User entities and provides methods to find users by email, which is
     * essential for validating
     */
    private final UserRepository userRepository;

    /**
     * Password encoder for securely hashing user passwords before storing them in the database.
     * This encoder is used to ensure that user passwords are not stored in plain text, enhancing
     * the security of the application. Hashing means that even if the database is compromised, the actual passwords
     * cannot be easily retrieved, as they are stored in a hashed format.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user based on the provided registration request. This method performs several steps to ensure
     * that the registration process is secure and valid: <br/>
     * 1. It checks if a user with the provided email already exists in the database <br/>
     * 2. If the email is already in use, it throws an IllegalArgumentException to indicate that registration cannot proceed. <br/>
     * 2. If the email is not in use, it creates a new User entity using the information from the registration request. <br/>
     * 3. The password from the registration request is encoded using the PasswordEncoder before being set on the User entity. <br/>
     * 4. Finally, the new User entity is saved to the database using the UserRepository, and the saved User object is returned. <br/>
     * The Transactional annotation ensures that the entire registration process is executed within a single transaction,
     * providing atomicity and consistency.
     * @param registrationRequest a data transfer object containing the necessary information for registering a new
     *                            user, including username, email, and password.
     * @return the User object representing the newly created user after it has been saved to the database.
     */
    @Override
    @Transactional
    public User register(RegistrationRequest registrationRequest) {

        // Check if a user with the provided email already exists in the database.
        // If such a user exists, throw an exception to prevent duplicate registrations.
        if(userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        // Create a new User entity using the information from the registration request.
        // The password is encoded using the PasswordEncoder to ensure it is stored securely.
        User newUser = User.builder()
                .username(registrationRequest.getUsername())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .build();

        // Save the new User entity to the database and return the saved User object.
        return userRepository.save(newUser);
    }
}
