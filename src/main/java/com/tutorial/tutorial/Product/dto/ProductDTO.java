package com.tutorial.tutorial.Product.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ProductDTO {
    @NotNull(message = "Please input code of product")
    private String productCode;
    
    @NotNull(message = "Please input name of product")
    @NotEmpty(message = "Product cannot be empty")
    private String name;
    
    @Min(1)
    private double price;
    
    @NotNull(message = "Please upload file")
    private MultipartFile image;
    
    private Long categoryId;
    
    public ProductDTO() {
    }
    
    public ProductDTO(String proCode, String name, double price, MultipartFile image) {
        this.productCode = proCode;
        this.name = name;
        this.price = price;
        this.image = image;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String code) {
        this.productCode = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
