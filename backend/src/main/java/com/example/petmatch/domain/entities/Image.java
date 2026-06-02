package com.example.petmatch.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing an image associated with an advertisement.
 * Each advertisement can have multiple images, and each image tracks its URL,
 * whether it's the primary image for the advertisement, and when it was created.
 */
@Entity
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Image {

    /**
     * Unique identifier for the image, generated as a UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Reference to the advertisement this image belongs to.
     * Multiple images can belong to the same advertisement.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement advertisement;

    /**
     * URL or file path where the image is stored.
     * This could be a local file path, S3 URL, or any other image storage location.
     * Uses TEXT type to support large base64-encoded images.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    /**
     * Flag indicating whether this is the primary/main image for the advertisement.
     * Each advertisement should have at most one primary image to display as the thumbnail.
     */
    @Column(nullable = false)
    private boolean isPrimary;

    /**
     * Timestamp indicating when the image was created/uploaded.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Automatically set the creation timestamp when the image entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
