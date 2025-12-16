package com.tutorial.tutorial.Product.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tutorial.tutorial.Category.entity.CategoryEntity;
import com.tutorial.tutorial.Category.repository.CategoryRepository;
import com.tutorial.tutorial.Product.dto.ProductDTO;
import com.tutorial.tutorial.Product.entity.ProductEntity;
import com.tutorial.tutorial.Product.repository.ProductRepository;

@Service
public class ProductService {

    private static final String UPLOAD_PATH = "images/";

    // allow to access repository methods (depdendency injection in spring)
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get all products
     */
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Find product by product code
     */
    public ProductEntity getProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }

    /**
     * Save a new product
     */
    public void saveProduct(ProductDTO productDTO) throws IOException {
        String imageUrl = uploadImage(productDTO.getImage());
        
        CategoryEntity category = null;
        if (productDTO.getCategoryId() != null) {
            category = categoryRepository.findById(productDTO.getCategoryId()).orElse(null);
        }
        
        ProductEntity entity = new ProductEntity(
            productDTO.getProductCode(),
            productDTO.getName(),
            productDTO.getPrice(),
            imageUrl,
            category
        );
        
        productRepository.save(entity);
    }

    /**
     * Update an existing product
     */
    public void updateProduct(String code, ProductDTO productDTO) throws IOException {
        ProductEntity productEntity = productRepository.findByProductCode(code);
        
        if (productEntity != null) {
            productEntity.setCode(productDTO.getProductCode());
            productEntity.setName(productDTO.getName());
            productEntity.setPrice(productDTO.getPrice());

            // Update category
            if (productDTO.getCategoryId() != null) {
                CategoryEntity category = categoryRepository.findById(productDTO.getCategoryId()).orElse(null);
                productEntity.setCategory(category);
            }

            // Update image only if a new one is uploaded
            if (productDTO.getImage() != null && !productDTO.getImage().isEmpty()) {
                String imageUrl = uploadImage(productDTO.getImage());
                productEntity.setImage(imageUrl);
            }

            productRepository.save(productEntity);
        }
    }

    /**
     * Delete a product by code
     */
    public void deleteProduct(String code) {
        ProductEntity productEntity = productRepository.findByProductCode(code);
        if (productEntity != null) {
            productRepository.delete(productEntity);
        }
    }

    /**
     * Search products by keyword
     */
    public List<ProductEntity> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.searchByKeyword(keyword);
    }

    /**
     * Filter products by category
     */
    public List<ProductEntity> getProductsByCategory(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return productRepository.findAll();
        }
        return productRepository.findByCategory(category);
    }

    /**
     * Search and filter products by category and keyword
     */
    public List<ProductEntity> searchAndFilterProducts(Long categoryId, String keyword) {
        if (categoryId != null && (keyword != null && !keyword.trim().isEmpty())) {
            return productRepository.searchByCategoryAndKeyword(categoryId, keyword);
        } else if (categoryId != null) {
            return getProductsByCategory(categoryId);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            return searchProducts(keyword);
        }
        return productRepository.findAll();
    }

    /**
     * Upload image file and return URL
     */
    private String uploadImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return "";
        }

        // Create path
        Path pathImage = Paths.get(UPLOAD_PATH + image.getOriginalFilename());

        // Create directory if it doesn't exist
        Files.createDirectories(pathImage.getParent());

        // Write file
        Files.write(pathImage, image.getBytes());

        return "/files/" + image.getOriginalFilename();
    }
}
