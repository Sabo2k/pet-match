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
                .name(category.getName())
                .build();
    }

    @Override
    public Category toEntity(CreateCategoryRequest createCategoryRequest) {
        return null;
    }
}
