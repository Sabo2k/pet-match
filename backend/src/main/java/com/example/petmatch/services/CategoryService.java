package com.example.petmatch.services;

import com.example.petmatch.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<Category> getAllCategories();
    Category createCategory(Category category);
    void deleteCategoryById(UUID id);
    Category getCategoryById(UUID id);
}
