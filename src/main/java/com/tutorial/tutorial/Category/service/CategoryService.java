package com.tutorial.tutorial.Category.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tutorial.tutorial.Category.entity.CategoryEntity;
import com.tutorial.tutorial.Category.repository.CategoryRepository;
import com.tutorial.tutorial.Product.repository.ProductRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all categories
     */
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Get categories with pagination
     */
    public Page<CategoryEntity> getCategoriesWithPagination(Pageable pageable) {
        return categoryRepository.findAllWithProducts(pageable);
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
     * @return true if deleted successfully, false if category has products
     */
    public boolean deleteCategory(Long id) {
        CategoryEntity category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return false;
        }
        
        // Check if category has products
        long productCount = productRepository.countByCategoryId(id);
        if (productCount > 0) {
            return false;
        }
        
        categoryRepository.deleteById(id);
        return true;
    }
}
