package com.tutorial.tutorial.Product.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.tutorial.Product.dto.ProductDTO;
import com.tutorial.tutorial.Product.entity.ProductEntity;
import com.tutorial.tutorial.Product.service.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class ProductController {

    private static final String UPLOAD_PATH = "images/";

    @Autowired
    private ProductService productService;

    /**
     * List all products with search, filter and pagination
     */
    @GetMapping("/products")
    public ResponseEntity<?> getProducts(@RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) Long categoryId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "id") String sortBy,
                                         @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductEntity> products = productService.getProductsWithPagination(categoryId, keyword, pageable);
        return ResponseEntity.ok(products);
    }

    /**
     * Display create product form
     */
    @PostMapping(value = "/products", consumes = "multipart/form-data")
    public ResponseEntity<?> saveProduct(@ModelAttribute @Valid ProductDTO productDTO) throws IOException {
        if (productDTO.getImage() == null || productDTO.getImage().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Please upload an image");
        }

        productService.saveProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created");
    }

    @GetMapping("/products/{code}")
    public ResponseEntity<?> getProduct(@PathVariable("code") String code) {
        ProductEntity productEntity = productService.getProductByCode(code);
        if (productEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        return ResponseEntity.ok(productEntity);
    }

    @PutMapping(value = "/products/{code}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateProduct(@PathVariable("code") String code,
                                          @ModelAttribute @Valid ProductDTO productDTO) throws IOException {
        productService.updateProduct(code, productDTO);
        return ResponseEntity.ok("Product updated");
    }

    @DeleteMapping("/products/{code}")
    public ResponseEntity<?> deleteProduct(@PathVariable("code") String code) {
        productService.deleteProduct(code);
        return ResponseEntity.ok("Product deleted");
    }

    /**
     * Serve uploaded image files
     */
    @GetMapping("/images/{file:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable("file") String filename) {
        try {
            Path imageFile = Paths.get(UPLOAD_PATH).resolve(filename);
            Resource resource = new UrlResource(imageFile.toUri());

            System.out.println("file name: " + filename );
            
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return ResponseEntity.badRequest().build();
    }
}
