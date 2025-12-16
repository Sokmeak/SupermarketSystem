package com.tutorial.tutorial.Product.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tutorial.tutorial.Category.entity.CategoryEntity;
import com.tutorial.tutorial.Category.service.CategoryService;
import com.tutorial.tutorial.Product.dto.ProductDTO;
import com.tutorial.tutorial.Product.entity.ProductEntity;
import com.tutorial.tutorial.Product.service.ProductService;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    private static final String UPLOAD_PATH = "images/";

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /**
     * List all products with search and filter
     */
    @GetMapping("/")
    public String getProducts(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) Long categoryId,
                             Model model) {
        List<ProductEntity> products;
        
        if (keyword != null || categoryId != null) {
            products = productService.searchAndFilterProducts(categoryId, keyword);
        } else {
            products = productService.getAllProducts();
        }
        
        List<CategoryEntity> categories = categoryService.getAllCategories();
        
        model.addAttribute("titleHeader", "List Product");
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("content", "fragments/list_product");
        return "products";
    }

    /**
     * Display create product form
     */
    @GetMapping("/create-form")
    public String getProductForm(Model model) {
        List<CategoryEntity> categories = categoryService.getAllCategories();
        model.addAttribute("productForm", new ProductDTO());
        model.addAttribute("categories", categories);
        model.addAttribute("titleHeader", "Create Product");
        model.addAttribute("content", "fragments/form");
        return "products";
    }

    /**
     * Save new product
     */
    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute @Valid ProductDTO productDTO, 
                             BindingResult bindingResult, 
                             Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            List<CategoryEntity> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            model.addAttribute("titleHeader", "Create Product");
            model.addAttribute("content", "fragments/form");
            return "products";
        }
        
        if (productDTO.getImage().isEmpty()) {
            List<CategoryEntity> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            model.addAttribute("error", "Please upload an image");
            model.addAttribute("titleHeader", "Create Product");
            model.addAttribute("content", "fragments/form");
            return "products";
        }

        productService.saveProduct(productDTO);
        return "redirect:/";
    }

    /**
     * Display edit product form
     */
    @GetMapping("/edit/{code}")
    public String editProductForm(@PathVariable("code") String code, Model model) {
        ProductEntity productEntity = productService.getProductByCode(code);
        
        if (productEntity == null) {
            return "redirect:/";
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductCode(productEntity.getCode());
        productDTO.setName(productEntity.getName());
        productDTO.setPrice(productEntity.getPrice());
        
        if (productEntity.getCategory() != null) {
            productDTO.setCategoryId(productEntity.getCategory().getId());
        }

        List<CategoryEntity> categories = categoryService.getAllCategories();
        model.addAttribute("productupdateForm", productDTO);
        model.addAttribute("categories", categories);
        model.addAttribute("titleHeader", "Edit Product");
        model.addAttribute("content", "fragments/editForm");
        return "products";
    }

    /**
     * Update existing product
     */
    @PostMapping("/edit/{code}")
    public String updateProduct(@PathVariable("code") String code,
                               @ModelAttribute @Valid ProductDTO productDTO,
                               BindingResult bindingResult,
                               Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            List<CategoryEntity> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            model.addAttribute("titleHeader", "Edit Product");
            model.addAttribute("content", "fragments/editForm");
            return "products";
        }

        productService.updateProduct(code, productDTO);
        return "redirect:/";
    }

    /**
     * Delete product
     */
    @GetMapping("/remove/{code}")
    public String deleteProduct(@PathVariable("code") String code) {
        productService.deleteProduct(code);
        return "redirect:/";
    }

    /**
     * Serve uploaded image files
     */
    @GetMapping("/files/{file:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable("file") String filename) {
        try {
            Path imageFile = Paths.get(UPLOAD_PATH).resolve(filename);
            Resource resource = new UrlResource(imageFile.toUri());
            
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
