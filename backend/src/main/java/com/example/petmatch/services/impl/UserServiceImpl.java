package com.example.petmatch.services.impl;

import com.example.petmatch.domain.entities.Advertisement;
import com.example.petmatch.domain.entities.User;
import com.example.petmatch.repositories.AdvertisementRepository;
import com.example.petmatch.repositories.UserRepository;
import com.example.petmatch.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UUID> getSavedAdvertisementIds(User user) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return managedUser.getSavedAdvertisements().stream()
                .map(Advertisement::getId)
                .toList();
    }

    @Override
    @Transactional
    public void saveAdvertisement(User user, UUID advertisementId) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found"));

        boolean alreadySaved = managedUser.getSavedAdvertisements().stream()
                .anyMatch(a -> a.getId().equals(advertisementId));
        if (!alreadySaved) {
            managedUser.getSavedAdvertisements().add(advertisement);
            userRepository.save(managedUser);
        }
    }

    @Override
    @Transactional
    public void unsaveAdvertisement(User user, UUID advertisementId) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        managedUser.getSavedAdvertisements().removeIf(a -> a.getId().equals(advertisementId));
        userRepository.save(managedUser);
    }
}
