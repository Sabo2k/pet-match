package com.example.petmatch.services.impl;

import com.example.petmatch.domain.CreateAdvertisementRequest;
import com.example.petmatch.domain.UpdateAdvertisementRequest;
import com.example.petmatch.domain.entities.Advertisement;
import com.example.petmatch.domain.entities.User;
import com.example.petmatch.repositories.AdvertisementRepository;
import com.example.petmatch.services.AdvertisementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

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
        return advertisementRepository.save(newAdvertisement);
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
    public void deleteAdvertisementById(UUID id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found with id: " + id));
        advertisementRepository.delete(advertisement);
    }
}
