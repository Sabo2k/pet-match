package com.example.petmatch.services.impl;

import com.example.petmatch.domain.dtos.UserProfileDto;
import com.example.petmatch.domain.entities.Advertisement;
import com.example.petmatch.domain.entities.User;
import com.example.petmatch.repositories.AdvertisementRepository;
import com.example.petmatch.repositories.UserRepository;
import com.example.petmatch.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileDto getProfile(User user) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return UserProfileDto.builder()
                .id(managedUser.getId())
                .username(managedUser.getUsername())
                .email(managedUser.getEmail())
                .build();
    }

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
    public UserProfileDto getProfileById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

    @Override
    public List<Advertisement> getCreatedAdvertisements(User user) {
        return advertisementRepository.findByAuthor_Id(user.getId());
    }

    @Override
    public List<Advertisement> getAdvertisementsByUserId(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return advertisementRepository.findByAuthor_Id(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Advertisement> getSavedAdvertisementDetails(User user) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return List.copyOf(managedUser.getSavedAdvertisements());
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

    @Override
    @Transactional
    public void changePassword(User user, String currentPassword, String newPassword) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!passwordEncoder.matches(currentPassword, managedUser.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        managedUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(managedUser);
    }

    @Override
    @Transactional
    public void deleteAccount(User user) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        managedUser.getSavedAdvertisements().clear();
        userRepository.delete(managedUser);
    }

    @Override
    @Transactional
    public void changeEmail(User user, String newEmail) {
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        managedUser.setEmail(newEmail);
        userRepository.save(managedUser);
    }
}
