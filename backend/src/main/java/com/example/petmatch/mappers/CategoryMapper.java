package com.example.petmatch.mappers;

import com.example.petmatch.domain.dtos.CategoryDto;
import com.example.petmatch.domain.dtos.CreateCategoryRequest;
import com.example.petmatch.domain.entities.Category;


public interface CategoryMapper {

    CategoryDto toDto(Category category);
    Category toEntity(CreateCategoryRequest createCategoryRequest);
}
