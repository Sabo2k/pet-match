package com.example.petmatch.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisementDto {

    private UUID id;
    private String title;
    private String description;
    private int age;
    private double price;
    private String location;
    private AuthorDto author;
    private CategoryDto category;
    private List<ImageDto> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
