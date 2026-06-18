package com.example.petmatch.services;

import com.example.petmatch.domain.dtos.UserProfileDto;
import com.example.petmatch.domain.entities.Advertisement;
import com.example.petmatch.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserProfileDto getProfile(User user);
    UserProfileDto getProfileById(UUID userId);
    List<UUID> getSavedAdvertisementIds(User user);
    List<Advertisement> getCreatedAdvertisements(User user);
    List<Advertisement> getAdvertisementsByUserId(UUID userId);
    List<Advertisement> getSavedAdvertisementDetails(User user);
    void saveAdvertisement(User user, UUID advertisementId);
    void unsaveAdvertisement(User user, UUID advertisementId);
}
