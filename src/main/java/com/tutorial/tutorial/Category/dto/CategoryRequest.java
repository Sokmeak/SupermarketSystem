package com.tutorial.tutorial.Category.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CategoryRequest {
    
    @Schema(description = "Category name", example = "Electronics", required = true)
    private String name;
    
    @Schema(description = "Category description", example = "Electronic devices and accessories")
    private String description;
    
    public CategoryRequest() {
    }
    
    public CategoryRequest(String name, String description) {
        this.name = name;
        this.description = description;
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
}
