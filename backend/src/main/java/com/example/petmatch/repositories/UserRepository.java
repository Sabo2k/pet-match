package com.example.petmatch.repositories;

import com.example.petmatch.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing User entities in the database.
 * This interface extends JpaRepository, providing CRUD operations and additional query methods for User entities.
 * It includes a custom method to find a user by their email address, which is commonly used for authentication and
 * registration processes.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Finds a user by their email address. This method is used to retrieve a user based on their unique email,
     * which is often required for authentication and registration processes.
     * The method returns an Optional<User>, which allows for handling cases where a user with the specified email
     * may not exist in the database, avoiding potential NullPointerExceptions and providing a more robust way to
     * handle user retrieval.
     * @param email the email address of the user to be retrieved from the database.
     *              This parameter is expected to be unique for each user.
     * @return an Optional containing the User object if a user with the specified email exists, or an empty
     *         Optional if no such user is found in the database.
     */
    Optional<User> findByEmail(String email);
}
