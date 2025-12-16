package com.tutorial.tutorial.Category.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tutorial.tutorial.Category.entity.CategoryEntity;
import com.tutorial.tutorial.Category.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get all categories
     */
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Get category by ID
     */
    public CategoryEntity getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    /**
     * Save a new category
     */
    public void saveCategory(CategoryEntity category) {
        categoryRepository.save(category);
    }

    /**
     * Update category
     */
    public void updateCategory(Long id, String name, String description) {
        CategoryEntity category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            category.setName(name);
            category.setDescription(description);
            categoryRepository.save(category);
        }
    }

    /**
     * Delete category
     */
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
