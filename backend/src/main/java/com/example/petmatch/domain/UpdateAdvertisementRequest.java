package com.example.petmatch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAdvertisementRequest {
    private UUID id;
    private String title;
    private String description;
    private int age;
    private double price;
    private String location;
    private UUID categoryId;
    private List<ImageRequest> images;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ImageRequest {
        private String url;
        private boolean isPrimary;
    }
}
