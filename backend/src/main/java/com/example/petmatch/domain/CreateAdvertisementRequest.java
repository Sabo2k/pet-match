package com.example.petmatch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAdvertisementRequest {
    private String title;
    private String description;
    private int age;
    private double price;
    private String location;
    private List<CreateImageRequest> images;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateImageRequest {
        private String url;
        private boolean isPrimary;
    }
}
