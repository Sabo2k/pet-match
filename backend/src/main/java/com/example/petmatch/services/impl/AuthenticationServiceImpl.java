package com.example.petmatch.services.impl;

import com.example.petmatch.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service implementation for handling user authentication and JWT token management.
 * This class provides methods to authenticate users, generate JWT tokens, and validate tokens.
 * It uses Spring Security's AuthenticationManager to authenticate user credentials and UserDetailsService
 * to load user details for token generation and validation. The JWT tokens are signed using a secret key
 * and have a specified expiration time.
 * The service is annotated with "Service" to indicate that it's a Spring-managed component, and
 * "RequiredArgsConstructor" to automatically generate a constructor for the final fields, allowing for dependency
 * injection.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    /**
     * AuthenticationManager is a Spring Security component responsible for processing authentication requests.
     * It is used to authenticate user credentials (email and password) against the configured authentication provider
     * and is injected into this service to perform authentication operations. The AuthenticationManager will throw an
     * exception if authentication fails, which can be handled by the calling code to provide appropriate responses
     * to the client.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * UserDetailsService is a Spring Security interface that provides a method to load user-specific data.
     * It is used in this service to retrieve user details based on the username (email)
     * for both authentication and token validation processes. The UserDetailsService is injected into this service,
     * allowing it to delegate the task of loading user information to the configured implementation, which typically
     * interacts with the user repository to fetch user data from the database.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Secret key used for signing JWT tokens. This value is injected from the application properties (e.g.,
     * application.properties or application.yml) using the @Value annotation, allowing for external configuration of
     * the secret key without hardcoding it in the codebase. The secret key is crucial for the security of JWT tokens,
     * as it is used to sign the tokens and verify their integrity.
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Expiration time for JWT tokens in milliseconds.
     * This value is set to 24 hours (86400000 milliseconds) and determines how long a
     * generated JWT token will be valid before it expires.
     */
    private final Long jwtExpiryMs = 86400000L;

    /**
     * Authenticates a user based on the provided email and password. This method uses the AuthenticationManager to
     * perform the authentication process. If the authentication is successful, it retrieves the user details using
     * the UserDetailsService and returns them. If authentication fails, an exception will be thrown by the
     * AuthenticationManager, which can be handled by the calling code to provide appropriate feedback to the client
     * @param email the email address of the user attempting to authenticate. This is used as the username for
     *              authentication purposes.
     * @param password the password provided by the user for authentication. This is used in conjunction with the email
     *                 to verify the user's credentials.
     * @return UserDetails object containing the authenticated user's information if authentication is successful.
     *         This includes details such as username, password, and authorities.
     */
    @Override
    public UserDetails authenticate(String email, String password) {

        // Use the AuthenticationManager to authenticate the user's credentials.
        // This will check the provided email and password against the configured authentication provider (e.g., a
        // database or in-memory user store). If the credentials are valid, authentication will succeed; otherwise, an
        // exception will be thrown.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // If authentication is successful, load and return the user details using the UserDetailsService.
        // The UserDetailsService will fetch the user information based on the email (username) provided.
        // If authentication fails, an exception will be thrown by the AuthenticationManager,
        // and this line will not be reached.
        return userDetailsService.loadUserByUsername(email);
    }

    /**
     * Generates a JWT token for the authenticated user. This method creates a JWT token containing the user's username
     * as the subject, along with the issued date and expiration date. The token is signed using the secret key and the
     * HS256 algorithm. The generated token can be used by the client for subsequent authenticated requests to
     * @param userDetails
     * @return
     */
    @Override
    public String generateToken(UserDetails userDetails) {

        // Create an empty claims map to hold any additional information that may be included in the JWT token.
        Map<String, Object> claims = new HashMap<>();

        // Build and return the JWT token using the Jwts builder. The token includes the claims, subject (username),
        // issued date, expiration date, and is signed with the secret key using the HS256 algorithm.
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a JWT token and retrieves the associated user details. This method parses the provided JWT token to
     * extract the username (subject) and then uses the UserDetailsService to load the user details based on that
     * username. If the token is valid and the user exists, it returns the UserDetails object; otherwise, it may throw
     * an exception if the token is invalid or if the user cannot be found.
     * @param token the JWT token to be validated. This token is expected to contain the user's username as the subject
     *              and is signed with the secret key.
     * @return UserDetails object containing the user's information if the token is valid. This includes details such as
     *         username, password, and authorities.
     */
    @Override
    public UserDetails validateToken(String token) {
        // Extract the username from the JWT token using the extractUsername method, which parses the token and
        // retrieves the subject. Then, use the UserDetailsService to load and return the user details based on the
        // extracted username. If the token is invalid or if the user cannot be found, an exception may be thrown during
        // parsing or loading.
        String username = extractUsername(token);
        // Load and return the user details using the UserDetailsService based on the extracted username.
        return userDetailsService.loadUserByUsername(username);
    }

    /**
     * Extracts the username (subject) from a JWT token. This method parses the JWT token using the secret key to verify
     * its integrity. If the token is valid, it retrieves the claims from the token and returns the subject, which is
     * the username of the user.
     * @param token the JWT token from which to extract the username. This token is expected to be signed with the
     *              secret key and contain the username as the subject.
     * @return the username extracted from the JWT token if the token is valid. If the token is invalid, an exception
     *         may be thrown during parsing.
     */
    private String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Generates the signing key for JWT token creation and validation. This method converts the secret key string into
     * a byte array
     * @return a Key object that can be used for signing JWT tokens. The key is generated using the secret key string
     *         and the HMAC SHA algorithm (HS256) provided by the Keys utility class from the JJWT library.
     */
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}