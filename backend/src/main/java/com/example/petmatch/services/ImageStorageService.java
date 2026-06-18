package com.example.petmatch.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    String store(MultipartFile file);
    void delete(String storedFilename);
}
