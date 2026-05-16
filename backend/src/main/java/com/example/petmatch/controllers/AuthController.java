package com.example.petmatch.controllers;

import com.example.petmatch.domain.dtos.AuthResponse;
import com.example.petmatch.domain.dtos.LoginRequest;
import com.example.petmatch.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling authentication-related endpoints, specifically user login.
 * It provides an endpoint for users to submit their login credentials and receive a JWT token upon successful
 * authentication.
 * The controller delegates the authentication logic to the AuthenticationService, which handles the actual verification
 * of credentials and token generation.
 */
@RestController
@RequestMapping(path = "/api/v1/auth/login")
@RequiredArgsConstructor
public class AuthController {

    /**
     * Service responsible for handling authentication logic, including verifying user credentials and generating JWT
     * tokens. This service is injected into the controller using constructor injection, allowing the controller to
     * delegate authentication tasks to it.
     * The AuthenticationService is expected to contain the business logic for authenticating users and generating
     * tokens, such as checking the provided email and password against stored user data and creating a JWT token with
     * the appropriate claims and expiration time.
     */
    private final AuthenticationService authenticationService;

    /**
     * Endpoint for user login. This method handles POST requests to the /api/v1/auth/login endpoint.
     * It takes a LoginRequest object as input, which contains the user's email and password. The method uses the
     * AuthenticationService to authenticate the user's credentials and, if successful, generates a JWT token. The
     * response includes the token and its expiration time.
     * @param loginRequest a data transfer object that holds the user's login credentials, specifically email and
     *                     password, sent by the client for authentication.
     * @return a ResponseEntity containing an AuthResponse with the generated JWT token and its expiration time if
     *         authentication is successful, or an appropriate error response if authentication fails due to invalid
     *         credentials.
     */
    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        UserDetails userDetails = authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        String tokenValue = authenticationService.generateToken(userDetails);

        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();

        return ResponseEntity.ok(authResponse);
    }
}