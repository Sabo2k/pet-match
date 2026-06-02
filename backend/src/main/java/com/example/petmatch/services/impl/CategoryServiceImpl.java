package com.example.petmatch.services.impl;

import com.example.petmatch.domain.entities.Category;
import com.example.petmatch.services.CategoryService;
import com.example.petmatch.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(Category category) {
        String categoryName = category.getName();
        if(categoryRepository.existsByNameIgnoreCase(categoryName)) {
            throw new IllegalArgumentException("Category with name '" + categoryName + "' already exists.");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()) {
            if(!category.get().getAdvertisements().isEmpty()) {
                throw new IllegalStateException("Cannot delete category with associated advertisements.");
            }
            categoryRepository.deleteById(id);
        }

    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id" + id));
    }
}
