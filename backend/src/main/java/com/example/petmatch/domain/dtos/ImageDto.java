package com.example.petmatch.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {

    private UUID id;
    private String imageUrl;
    private boolean isPrimary;
    private LocalDateTime createdAt;
}
