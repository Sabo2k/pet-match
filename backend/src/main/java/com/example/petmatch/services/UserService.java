package com.example.petmatch.services;

import com.example.petmatch.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UUID> getSavedAdvertisementIds(User user);
    void saveAdvertisement(User user, UUID advertisementId);
    void unsaveAdvertisement(User user, UUID advertisementId);
}
