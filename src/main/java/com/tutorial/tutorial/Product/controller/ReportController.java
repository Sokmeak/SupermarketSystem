package com.tutorial.tutorial.Product.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.tutorial.Product.entity.ProductEntity;
import com.tutorial.tutorial.Product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Dashboard and reporting APIs")
public class ReportController {

    @Autowired
    private ProductService productService;

    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary", description = "Retrieve summary statistics for dashboard")
    public ResponseEntity<Map<String, Object>> getSummary() {
        List<ProductEntity> allProducts = productService.getAllProducts();
        
        int totalItems = allProducts.size();
        long lowStock = allProducts.stream()
                .filter(p -> p.getPrice() < 5.0) // Consider products under $5 as low stock (adjust logic as needed)
                .count();
        
        double stockValue = allProducts.stream()
                .mapToDouble(ProductEntity::getPrice)
                .sum();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalItems", totalItems);
        summary.put("lowStock", lowStock);
        summary.put("stockValue", Math.round(stockValue * 100.0) / 100.0);
        
        return ResponseEntity.ok(summary);
    }
}
