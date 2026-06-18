package com.example.petmatch.services;

import com.example.petmatch.domain.CreateAdvertisementRequest;
import com.example.petmatch.domain.UpdateAdvertisementRequest;
import com.example.petmatch.domain.entities.Advertisement;
import com.example.petmatch.domain.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AdvertisementService {
    Advertisement getAdvertisementById(UUID id);
    List<Advertisement> getAllAdvertisements();
    Advertisement createAdvertisement(User user, CreateAdvertisementRequest advertisementRequest, List<MultipartFile> images, int primaryImageIndex);
    Advertisement updateAdvertisementById(UUID id, UpdateAdvertisementRequest advertisementRequest);
    void deleteAdvertisementById(UUID id);
}
