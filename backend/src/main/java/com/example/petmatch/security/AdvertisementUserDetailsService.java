package com.example.petmatch.security;

import com.example.petmatch.domain.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.petmatch.repositories.UserRepository;

/**
 * Service class that implements UserDetailsService to load user-specific data for authentication.
 * It retrieves user information from the database using the UserRepository and converts it into a format
 * that Spring Security can use for authentication and authorization processes.
 */
@RequiredArgsConstructor
public class AdvertisementUserDetailsService implements UserDetailsService {

    /**
     * The UserRepository is used to access user data from the database.
     * It allows the service to find users by their email and retrieve their details for authentication purposes.
     */
    private final UserRepository userRepository;

    /**
     * Loads the user details by email (used as username) from the database.
     * If the user is found, it returns an instance of AdvertisementUserDetails containing the user's information.
     * If the user is not found, it throws a UsernameNotFoundException with an appropriate message.
     * @param email The email of the user to be loaded, which serves as the username for authentication.
     * @return a UserDetails object containing the user's information for authentication and authorization.
     * @throws UsernameNotFoundException if no user is found with the provided email, indicating that authentication
     *                                   cannot proceed.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Use the UserRepository to find a user by their email. If the user is not found, throw an exception with a
        // message indicating that the user was not found. If the user is found, create and return a new
        // AdvertisementUserDetails object that contains the user's information for authentication and authorization
        // purposes.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new AdvertisementUserDetails(user);
    }
}
