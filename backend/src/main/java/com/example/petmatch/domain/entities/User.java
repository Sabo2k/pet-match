package com.example.petmatch.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * checks if two User objects are equal based on their fields.
     * @param objectToCompareTo object to be compared to
     * @return boolean, indicating whether the two users are identical
     */
    @Override
    public boolean equals(Object objectToCompareTo) {

        // If the object is the same instance, return true.
        if(this == objectToCompareTo) {
            return true;
        }

        // If the object is null or not a User, return false.
        if(objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) {
            return false;
        }

        // Cast the object to User...
        User user = (User) objectToCompareTo;

        // ...and compare all fields
        return Objects.equals(id, user.id) &&
               Objects.equals(email, user.email) &&
               Objects.equals(password, user.password) &&
               Objects.equals(username, user.username) &&
               Objects.equals(createdAt, user.createdAt);
    }

    /**
     * generates a unique integer value (hash code) for
     * hash-based collections to efficiently store
     * and retreive objects
     * @return unique integer value
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, username, createdAt);
    }

    /**
     * every time a user object is saved into
     * the database where the createdAt member
     * is null, populate createdAt
     * with the current datetime
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
