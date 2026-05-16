package com.example.petmatch.controllers;

import com.example.petmatch.domain.dtos.RegistrationRequest;
import com.example.petmatch.domain.entities.User;
import com.example.petmatch.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling user registration endpoints.
 * It provides an endpoint for users to submit their registration details and create a new account.
 * The controller delegates the registration logic to the RegistrationService, which handles the actual process of
 * validating the registration data and creating a new user in the system.
 */
@RestController
@RequestMapping(path = "/api/v1/auth/register")
@RequiredArgsConstructor
public class RegistrationController {

    /**
     * Service responsible for handling user registration logic, including validation and persistence of new user data.
     * This service is injected into the controller using constructor injection, allowing the controller to delegate
     * registration tasks to it.
     * The RegistrationService is expected to contain the business logic for creating new users, such as checking for
     * existing email addresses, validating input data, and saving the new user to the database.
     * This is an interface, and the actual implementation will be provided by a class that implements the
     * RegistrationService interface, such as RegistrationServiceImpl.
     */
    private final RegistrationService registrationService;

    /**
     * Endpoint for user registration. This method handles POST requests to the /api/v1/auth/register endpoint.
     * It takes a RegistrationRequest object as input, which contains the necessary information for registering a
     * new user (username, email, and password) and delegates the registration process to the RegistrationService.
     * If registration is successful, it returns a 201 Created response with the email of the created user in the body.
     * If registration fails due to invalid input (e.g., email already in use), it returns a bad request response with
     * an appropriate error message.
     * @param registrationRequest a data transfer object that holds data sent by the client for
     *                            user registration, including username, email, and password.
     * @return a ResponseEntity containing the email of the created user if registration is successful,
     *         or a bad request response with an error message if registration fails due to invalid input.
     */
    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
        try {
            // Delegate the registration process to the RegistrationService,
            // which will handle the business logic of creating a new user.
            User createdUser = registrationService.register(registrationRequest);

            // If registration is successful, return a 201 Created response with the email of the
            // created user in the body.
            return ResponseEntity.status(201).body(createdUser.getEmail());
        }
        catch(IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.badRequest().body(illegalArgumentException.getMessage());
        }
    }
}
