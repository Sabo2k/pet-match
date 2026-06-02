package com.example.petmatch.mappers.impl;

import com.example.petmatch.domain.dtos.CategoryDto;
import com.example.petmatch.domain.dtos.CreateCategoryRequest;
import com.example.petmatch.domain.entities.Category;
import com.example.petmatch.mappers.CategoryMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .advertisementCount(category.getAdvertisements().size())
                .build();
    }

    @Override
    public Category toEntity(CreateCategoryRequest createCategoryRequest) {
        return Category.builder()
                .name(createCategoryRequest.getName())
                .build();
    }
}
