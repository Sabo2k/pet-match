package com.example.petmatch.services.impl;

import com.example.petmatch.domain.CreateAdvertisementRequest;
import com.example.petmatch.domain.UpdateAdvertisementRequest;
import com.example.petmatch.domain.entities.Advertisement;
import com.example.petmatch.domain.entities.Image;
import com.example.petmatch.domain.entities.User;
import com.example.petmatch.repositories.AdvertisementRepository;
import com.example.petmatch.repositories.ImageRepository;
import com.example.petmatch.services.AdvertisementService;
import com.example.petmatch.services.CategoryService;
import com.example.petmatch.services.ImageStorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final ImageRepository imageRepository;
    private final CategoryService categoryService;
    private final ImageStorageService imageStorageService;

    @Override
    public List<Advertisement> getAllAdvertisements() {
        return advertisementRepository.findAll();
    }

    @Override
    public Advertisement getAdvertisementById(UUID id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found with id: " + id));
    }

    @Override
    @Transactional
    public Advertisement createAdvertisement(User user, CreateAdvertisementRequest advertisementRequest, List<MultipartFile> imageFiles, int primaryImageIndex) {
        Advertisement newAdvertisement = new Advertisement();
        newAdvertisement.setTitle(advertisementRequest.getTitle());
        newAdvertisement.setDescription(advertisementRequest.getDescription());
        newAdvertisement.setAge(advertisementRequest.getAge());
        newAdvertisement.setPrice(advertisementRequest.getPrice());
        newAdvertisement.setLocation(advertisementRequest.getLocation());
        newAdvertisement.setAuthor(user);

        if (advertisementRequest.getCategoryId() != null) {
            newAdvertisement.setCategory(categoryService.getCategoryById(advertisementRequest.getCategoryId()));
        }

        newAdvertisement.setImages(new ArrayList<>());

        Advertisement savedAdvertisement = advertisementRepository.save(newAdvertisement);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<Image> images = new ArrayList<>();
            for (int i = 0; i < imageFiles.size(); i++) {
                String storedFilename = imageStorageService.store(imageFiles.get(i));
                Image image = Image.builder()
                        .imageUrl("/images/" + storedFilename)
                        .isPrimary(i == primaryImageIndex)
                        .advertisement(savedAdvertisement)
                        .build();
                images.add(imageRepository.save(image));
            }
            savedAdvertisement.setImages(images);
        }

        return savedAdvertisement;
    }

    @Override
    @Transactional
    public Advertisement updateAdvertisementById(UUID id, UpdateAdvertisementRequest advertisementRequest) {
        Advertisement existingAdvertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found with id: " + id));

        existingAdvertisement.setTitle(advertisementRequest.getTitle());
        existingAdvertisement.setDescription(advertisementRequest.getDescription());
        existingAdvertisement.setAge(advertisementRequest.getAge());
        existingAdvertisement.setPrice(advertisementRequest.getPrice());
        existingAdvertisement.setLocation(advertisementRequest.getLocation());

        return advertisementRepository.save(existingAdvertisement);
    }

    @Override
    @Transactional
    public void deleteAdvertisementById(UUID id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found with id: " + id));

        // Delete image files from disk before removing the DB record
        for (Image image : advertisement.getImages()) {
            String filename = extractFilename(image.getImageUrl());
            imageStorageService.delete(filename);
        }

        advertisementRepository.delete(advertisement);
    }

    private String extractFilename(String imageUrl) {
        int lastSlash = imageUrl.lastIndexOf('/');
        return (lastSlash >= 0) ? imageUrl.substring(lastSlash + 1) : imageUrl;
    }
}
