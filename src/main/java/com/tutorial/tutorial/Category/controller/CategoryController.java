package com.tutorial.tutorial.Category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.tutorial.Category.dto.CategoryRequest;
import com.tutorial.tutorial.Category.dto.CategoryResponseDTO;
import com.tutorial.tutorial.Category.entity.CategoryEntity;
import com.tutorial.tutorial.Category.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Category management APIs")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Display all categories with pagination
     */
    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve list of all product categories with pagination")
    public ResponseEntity<?> getCategories(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "id") String sortBy,
                                           @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CategoryResponseDTO> categories = categoryService.getCategoriesWithPagination(pageable)
                .map(CategoryResponseDTO::new);
        return ResponseEntity.ok(categories);
    }

    /**
     * Display create category form
     */
    @PostMapping
    @Operation(summary = "Create category", description = "Add a new product category")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryRequest request) {
        CategoryEntity category = new CategoryEntity(request.getName(), request.getDescription());
        categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category created");
    }

    /**
     * Display edit category form
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update an existing category")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                            @RequestBody CategoryRequest request) {
        categoryService.updateCategory(id, request.getName(), request.getDescription());
        return ResponseEntity.ok("Category updated");
    }

    /**
     * Delete category
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Remove a category")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cannot delete category. It has products associated with it.");
        }
        return ResponseEntity.ok("Category deleted");
    }
}
