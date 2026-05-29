package com.example.petmatch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
