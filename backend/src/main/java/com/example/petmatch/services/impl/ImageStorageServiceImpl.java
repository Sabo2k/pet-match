package com.example.petmatch.services.impl;

import com.example.petmatch.services.ImageStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    private final Path uploadDir;

    public ImageStorageServiceImpl(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory: " + this.uploadDir, e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "image"
        );

        // Reject filenames with path traversal sequences
        if (originalFilename.contains("..")) {
            throw new IllegalArgumentException("Invalid filename: " + originalFilename);
        }

        String extension = getExtension(originalFilename);
        String storedFilename = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);

        try {
            Files.copy(file.getInputStream(), uploadDir.resolve(storedFilename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + storedFilename, e);
        }

        return storedFilename;
    }

    @Override
    public void delete(String storedFilename) {
        try {
            Files.deleteIfExists(uploadDir.resolve(storedFilename));
        } catch (IOException e) {
            // Deletion failure should not block other operations
        }
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex >= 0) ? filename.substring(dotIndex + 1).toLowerCase() : "";
    }
}
