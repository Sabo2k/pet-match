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

        // Use the AuthenticationService to authenticate the user's credentials (email and password) provided in the
        // LoginRequest. If authentication is successful, the service will return a UserDetails object containing the
        // user's information. If authentication fails, an exception will be thrown, which can be handled by the calling
        // code to return an appropriate error response to the client.
        UserDetails userDetails = authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        // If authentication is successful, generate a JWT token for the authenticated user using the
        // AuthenticationService.
        String tokenValue = authenticationService.generateToken(userDetails);

        // Create an AuthResponse object containing the generated token and its expiration time (in seconds).
        // The token is included in the response body, allowing the client to use it for subsequent authenticated
        // requests to protected endpoints. The expiration time is set to 86400 seconds (24 hours) in this example, but
        // it can be configured as needed.
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();

        // Return a ResponseEntity with the AuthResponse containing the token and expiration time.
        // The response status is set to 200 OK if authentication is successful, allowing the client to receive the
        // token and use it for authentication in future requests. If authentication fails, an appropriate error
        // response can be returned by catching the exception thrown by the AuthenticationService and returning a
        // ResponseEntity with an error status
        return ResponseEntity.ok(authResponse);
    }
}