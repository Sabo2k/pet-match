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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @GetMapping
    public ResponseEntity<List<AdvertisementDto>> getAllAdvertisements() {
        List<Advertisement> advertisements = advertisementService.getAllAdvertisements();
        List<AdvertisementDto> advertisementDtos = advertisements.stream()
                .map(this::convertToDto)
                .toList();
        return ResponseEntity.ok(advertisementDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDto> getAdvertisementById(@PathVariable UUID id) {
        Advertisement advertisement = advertisementService.getAdvertisementById(id);
        return ResponseEntity.ok(convertToDto(advertisement));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdvertisementDto> createAdvertisement(
            @RequestPart("data") CreateAdvertisementRequest createRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "primaryImageIndex", defaultValue = "0") int primaryImageIndex) {
        User currentUser = getCurrentUser();
        Advertisement createdAdvertisement = advertisementService.createAdvertisement(
                currentUser, createRequest, images, primaryImageIndex);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdAdvertisement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementDto> updateAdvertisement(
            @PathVariable UUID id,
            @RequestBody UpdateAdvertisementRequest updateRequest) {
        Advertisement updatedAdvertisement = advertisementService.updateAdvertisementById(id, updateRequest);
        return ResponseEntity.ok(convertToDto(updatedAdvertisement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable UUID id) {
        advertisementService.deleteAdvertisementById(id);
        return ResponseEntity.noContent().build();
    }

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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AdvertisementUserDetails userDetails = (AdvertisementUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
