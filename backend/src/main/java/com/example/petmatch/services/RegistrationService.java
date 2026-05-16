package com.example.petmatch.services;

import com.example.petmatch.domain.dtos.RegistrationRequest;
import com.example.petmatch.domain.entities.User;

/**
 * Service interface for handling user registration logic.
 * This interface defines the contract for registering new users, including validation and persistence of user data.
 * Implementations of this interface are expected to contain the business logic for creating new users, such
 * as checking for existing email addresses, validating input data, and saving the new user to the database.
 * The register method takes a RegistrationRequest object as input and returns a User object representing the newly
 * created user.
 */
public interface RegistrationService {

    /**
     * Registers a new user based on the provided registration request. This method is responsible for validating the
     * registration data, checking for existing users, encoding passwords, and saving the new user to the database.
     * @param registrationRequest a data transfer object containing the necessary information for registering a new
     *                            user, including username, email, and password.
     * @return the User object representing the newly created user after it has been saved to the database.
     */
    User register(RegistrationRequest registrationRequest);
}
