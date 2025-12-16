package com.tutorial.tutorial.Category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tutorial.tutorial.Category.entity.CategoryEntity;
import com.tutorial.tutorial.Category.service.CategoryService;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Display all categories
     */
    @GetMapping
    public String getCategories(Model model) {
        List<CategoryEntity> categories = categoryService.getAllCategories();
        model.addAttribute("titleHeader", "Category Management");
        model.addAttribute("categories", categories);
        model.addAttribute("content", "fragments/category_list");
        return "products";
    }

    /**
     * Display create category form
     */
    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        model.addAttribute("titleHeader", "Create Category");
        model.addAttribute("content", "fragments/category_form");
        return "products";
    }

    /**
     * Save new category
     */
    @PostMapping("/save")
    public String saveCategory(@RequestParam String name, 
                              @RequestParam String description) {
        CategoryEntity category = new CategoryEntity(name, description);
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    /**
     * Display edit category form
     */
    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        CategoryEntity category = categoryService.getCategoryById(id);
        if (category == null) {
            return "redirect:/categories";
        }
        model.addAttribute("category", category);
        model.addAttribute("titleHeader", "Edit Category");
        model.addAttribute("content", "fragments/category_edit");
        return "products";
    }

    /**
     * Update category
     */
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam String description) {
        categoryService.updateCategory(id, name, description);
        return "redirect:/categories";
    }

    /**
     * Delete category
     */
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}
