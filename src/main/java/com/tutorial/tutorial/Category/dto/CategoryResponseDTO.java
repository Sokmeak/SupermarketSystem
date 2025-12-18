package com.tutorial.tutorial.Category.dto;

import java.time.LocalDateTime;

import com.tutorial.tutorial.Category.entity.CategoryEntity;

import io.swagger.v3.oas.annotations.media.Schema;

public class CategoryResponseDTO{
    
    @Schema(description = "Category ID", example = "1")
    private Long id;
    
    @Schema(description = "Category name", example = "Electronics")
    private String name;
    
    @Schema(description = "Category description", example = "Electronic devices and gadgets")
    private String description;
    
    @Schema(description = "Number of products in this category", example = "5")
    private int productCount;
    
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    public CategoryResponseDTO() {
    }

    public CategoryResponseDTO(CategoryEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.productCount = entity.getProducts() != null ? entity.getProducts().size() : 0;
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
