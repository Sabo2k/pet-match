package com.example.petmatch.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "advertisements")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int age;

    private double price;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private boolean isReported;

    @Column(nullable = false)
    private int reportCount;

    /**
     * One-to-many relationship with Image entities.
     * Each advertisement can have multiple images.
     * CascadeType.ALL ensures that when an advertisement is deleted, all its associated images are also deleted.
     * orphanRemoval = true ensures that removing an image from the list also deletes it from the database.
     * FetchType.LAZY is used to optimize queries by loading images only when needed.
     */
    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
