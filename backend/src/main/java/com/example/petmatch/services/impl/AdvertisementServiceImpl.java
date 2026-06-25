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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final ImageRepository imageRepository;
    private final CategoryService categoryService;

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
    public Advertisement createAdvertisement(User user, CreateAdvertisementRequest advertisementRequest) {
        Advertisement newAdvertisement = new Advertisement();
        newAdvertisement.setTitle(advertisementRequest.getTitle());
        newAdvertisement.setDescription(advertisementRequest.getDescription());
        newAdvertisement.setAge(advertisementRequest.getAge());
        newAdvertisement.setPrice(advertisementRequest.getPrice());
        newAdvertisement.setLocation(advertisementRequest.getLocation());
        newAdvertisement.setAuthor(user);
        
        // Set the category if provided
        if (advertisementRequest.getCategoryId() != null) {
            newAdvertisement.setCategory(categoryService.getCategoryById(advertisementRequest.getCategoryId()));
        }
        
        newAdvertisement.setImages(new ArrayList<>());
        
        Advertisement savedAdvertisement = advertisementRepository.save(newAdvertisement);
        
        // Create and associate images if provided
        if (advertisementRequest.getImages() != null && !advertisementRequest.getImages().isEmpty()) {
            List<Image> images = new ArrayList<>();
            for (CreateAdvertisementRequest.CreateImageRequest imageRequest : advertisementRequest.getImages()) {
                Image image = Image.builder()
                        .imageUrl(imageRequest.getUrl())
                        .isPrimary(imageRequest.isPrimary())
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

        if (advertisementRequest.getImages() != null && !advertisementRequest.getImages().isEmpty()) {
            boolean hasExistingPrimary = existingAdvertisement.getImages().stream().anyMatch(Image::isPrimary);
            for (UpdateAdvertisementRequest.ImageRequest imageRequest : advertisementRequest.getImages()) {
                boolean setPrimary = imageRequest.isPrimary() && !hasExistingPrimary;
                Image image = Image.builder()
                        .imageUrl(imageRequest.getUrl())
                        .isPrimary(setPrimary)
                        .advertisement(existingAdvertisement)
                        .build();
                imageRepository.save(image);
                existingAdvertisement.getImages().add(image);
                if (setPrimary) hasExistingPrimary = true;
            }
        }

        return advertisementRepository.save(existingAdvertisement);
    }

    @Override
    public void deleteAdvertisementById(UUID id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found with id: " + id));
        advertisementRepository.delete(advertisement);
    }
}
