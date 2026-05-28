package com.example.petmatch.controllers;

import com.example.petmatch.domain.dtos.AuthResponse;
import com.example.petmatch.domain.dtos.LoginRequest;
import com.example.petmatch.services.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling authentication-related endpoints, including user login and logout.
 * It provides endpoints for users to submit their login credentials and receive a JWT token upon successful
 * authentication, as well as to logout and clear their authentication session.
 * The controller delegates the authentication logic to the AuthenticationService, which handles the actual verification
 * of credentials and token generation.
 */
@RestController
@RequestMapping(path = "/api/v1/auth")
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
     * AuthenticationService to authenticate the user's credentials and, if successful, generates a JWT token.
     * The token is set as an httpOnly, Secure cookie for secure transmission and to prevent XSS attacks.
     * @param loginRequest a data transfer object that holds the user's login credentials, specifically email and
     *                     password, sent by the client for authentication.
     * @param response the HTTP response object used to set the authentication cookie.
     * @return a ResponseEntity containing an AuthResponse with success status if authentication is successful,
     *         or an appropriate error response if authentication fails due to invalid credentials.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

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

        // Set the JWT token as an httpOnly, Secure cookie to prevent XSS attacks and ensure secure transmission
        // over HTTPS. The cookie will be automatically sent by the browser with each subsequent request to the server.
        response.addCookie(createAuthCookie(tokenValue));

        // Create an AuthResponse object with success status. The token is NOT included in the response body
        // for security reasons - it's only transmitted via the httpOnly cookie.
        AuthResponse authResponse = AuthResponse.builder()
                .token(null)  // Token is not sent in response body, only via secure httpOnly cookie
                .expiresIn(86400)  // Expiration time in seconds (24 hours)
                .build();

        // Return a ResponseEntity with the AuthResponse containing the expiration time.
        // The response status is set to 200 OK if authentication is successful, allowing the client to know
        // that the login was successful. The token itself is transmitted via the httpOnly cookie.
        return ResponseEntity.ok(authResponse);
    }

    /**
     * Helper method to create an httpOnly, Secure authentication cookie.
     * @param tokenValue the JWT token to be set in the cookie
     * @return a cookie with secure settings for XSS protection
     */
    private jakarta.servlet.http.Cookie createAuthCookie(String tokenValue) {
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("authToken", tokenValue);
        cookie.setHttpOnly(true);  // Prevent JavaScript access to the cookie (XSS protection)
        cookie.setSecure(true);    // Only transmit over HTTPS
        cookie.setPath("/");       // Available for all paths
        cookie.setMaxAge(86400);   // Expires in 24 hours
        cookie.setAttribute("SameSite", "Strict");  // CSRF protection
        return cookie;
    }

    /**
     * Endpoint for user logout. This method handles POST requests to the /api/v1/auth/logout endpoint.
     * It clears the authentication cookie by setting its maxAge to 0, effectively invalidating the token
     * stored in the cookie on the client side.
     * @param response the HTTP response object used to clear the authentication cookie.
     * @return a ResponseEntity with success status indicating the user has been logged out.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // Create a cookie with the same name and path but with maxAge set to 0 to delete it
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("authToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);  // Setting maxAge to 0 deletes the cookie
        cookie.setAttribute("SameSite", "Strict");
        
        response.addCookie(cookie);
        
        return ResponseEntity.ok("Logged out successfully");
    }
}