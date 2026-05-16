package com.example.petmatch.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user registration requests.
 * This class encapsulates the necessary information for registering a new user, including username, email, and
 * password.
 * It is used to transfer data from the client to the server during the registration process.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
}
