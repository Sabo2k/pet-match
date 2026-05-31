package com.example.petmatch.controllers;

import com.example.petmatch.domain.CreateAdvertisementRequest;
import com.example.petmatch.domain.UpdateAdvertisementRequest;
import com.example.petmatch.domain.dtos.AdvertisementDto;
import com.example.petmatch.domain.dtos.AuthorDto;
import com.example.petmatch.domain.dtos.ImageDto;
import com.example.petmatch.domain.entities.Advertisement;
import com.example.petmatch.domain.entities.User;
import com.example.petmatch.security.AdvertisementUserDetails;
import com.example.petmatch.services.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    /**
     * Retrieves all advertisements.
     * @return a list of all advertisements
     */
    @GetMapping
    public ResponseEntity<List<AdvertisementDto>> getAllAdvertisements() {
        List<Advertisement> advertisements = advertisementService.getAllAdvertisements();
        List<AdvertisementDto> advertisementDtos = advertisements.stream()
                .map(this::convertToDto)
                .toList();
        return ResponseEntity.ok(advertisementDtos);
    }

    /**
     * Retrieves a single advertisement by ID.
     * @param id the UUID of the advertisement to retrieve
     * @return the advertisement with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDto> getAdvertisementById(@PathVariable UUID id) {
        Advertisement advertisement = advertisementService.getAdvertisementById(id);
        return ResponseEntity.ok(convertToDto(advertisement));
    }

    /**
     * Creates a new advertisement.
     * Requires authentication. The authenticated user becomes the author of the advertisement.
     * @param createRequest the request containing advertisement details
     * @return the created advertisement with HTTP 201 Created status
     */
    @PostMapping
    public ResponseEntity<AdvertisementDto> createAdvertisement(
            @RequestBody CreateAdvertisementRequest createRequest) {
        User currentUser = getCurrentUser();
        Advertisement createdAdvertisement = advertisementService.createAdvertisement(currentUser, createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdAdvertisement));
    }

    /**
     * Updates an existing advertisement.
     * Requires authentication.
     * @param id the UUID of the advertisement to update
     * @param updateRequest the request containing updated advertisement details
     * @return the updated advertisement
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementDto> updateAdvertisement(
            @PathVariable UUID id,
            @RequestBody UpdateAdvertisementRequest updateRequest) {
        Advertisement updatedAdvertisement = advertisementService.updateAdvertisementById(id, updateRequest);
        return ResponseEntity.ok(convertToDto(updatedAdvertisement));
    }

    /**
     * Deletes an advertisement.
     * Requires authentication.
     * @param id the UUID of the advertisement to delete
     * @return HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable UUID id) {
        advertisementService.deleteAdvertisementById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Converts an Advertisement entity to an AdvertisementDto.
     * @param advertisement the advertisement entity to convert
     * @return the advertisement DTO
     */
    private AdvertisementDto convertToDto(Advertisement advertisement) {
        AuthorDto authorDto = null;
        if (advertisement.getAuthor() != null) {
            authorDto = AuthorDto.builder()
                    .id(advertisement.getAuthor().getId())
                    .username(advertisement.getAuthor().getUsername())
                    .build();
        }

        List<ImageDto> imageDtos = advertisement.getImages().stream()
                .map(image -> ImageDto.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .isPrimary(image.isPrimary())
                        .createdAt(image.getCreatedAt())
                        .build())
                .toList();

        return AdvertisementDto.builder()
                .id(advertisement.getId())
                .title(advertisement.getTitle())
                .description(advertisement.getDescription())
                .age(advertisement.getAge())
                .price(advertisement.getPrice())
                .location(advertisement.getLocation())
                .author(authorDto)
                .images(imageDtos)
                .createdAt(advertisement.getCreatedAt())
                .updatedAt(advertisement.getUpdatedAt())
                .build();
    }

    /**
     * Retrieves the currently authenticated user.
     * @return the User object of the authenticated user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AdvertisementUserDetails userDetails = (AdvertisementUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
