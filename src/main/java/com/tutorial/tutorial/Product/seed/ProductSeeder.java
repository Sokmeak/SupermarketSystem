package com.tutorial.tutorial.Product.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.tutorial.tutorial.Category.entity.CategoryEntity;
import com.tutorial.tutorial.Category.repository.CategoryRepository;
import com.tutorial.tutorial.Product.entity.ProductEntity;
import com.tutorial.tutorial.Product.repository.ProductRepository;

@Component
@Order(2) // Run after category seeder
public class ProductSeeder implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if products already exist
        if (productRepository.count() > 0) {
            System.out.println("Products already seeded. Skipping initialization.");
            return;
        }

        System.out.println("Seeding initial products...");

        // Get categories
        CategoryEntity electronics = categoryRepository.findByName("Electronics");
        CategoryEntity plastics = categoryRepository.findByName("Plastics");
        CategoryEntity textiles = categoryRepository.findByName("Textiles");
        CategoryEntity office = categoryRepository.findByName("Office Supplies");
        CategoryEntity food = categoryRepository.findByName("Food & Beverages");
        CategoryEntity automotive = categoryRepository.findByName("Automotive");

        // Seed 15 products across various categories
        seedProduct("LAPTOP001", "Dell XPS 13 Laptop", "High-performance ultrabook with Intel i7", 1299.99, electronics);
        seedProduct("PHONE001", "iPhone 15 Pro", "Latest Apple smartphone with A17 chip", 999.99, electronics);
        seedProduct("HDMI001", "HDMI Cable 2m", "4K compatible HDMI cable", 12.99, electronics);
        
        seedProduct("BOTTLE001", "Water Bottle 1L", "BPA-free plastic water bottle", 8.99, plastics);
        seedProduct("CONTAINER001", "Food Container Set", "Set of 5 stackable containers", 24.99, plastics);
        
        seedProduct("TSHIRT001", "Cotton T-Shirt", "100% organic cotton, various colors", 19.99, textiles);
        seedProduct("JEANS001", "Denim Jeans", "Classic fit denim jeans", 49.99, textiles);
        
        seedProduct("PEN001", "Ballpoint Pen Pack", "Pack of 10 blue pens", 4.99, office);
        seedProduct("NOTEBOOK001", "A4 Notebook", "200-page ruled notebook", 6.99, office);
        seedProduct("STAPLER001", "Office Stapler", "Heavy-duty metal stapler", 14.99, office);
        
        seedProduct("COFFEE001", "Ground Coffee 500g", "Premium arabica ground coffee", 18.99, food);
        seedProduct("SNACK001", "Mixed Nuts 250g", "Roasted and salted mixed nuts", 12.99, food);
        
        seedProduct("OIL001", "Engine Oil 5W-30", "Synthetic motor oil 5L", 34.99, automotive);
        seedProduct("FILTER001", "Air Filter", "Universal car air filter", 22.99, automotive);
        seedProduct("WIPER001", "Windshield Wipers", "Set of 2 wiper blades", 29.99, automotive);

        System.out.println("Products seeded successfully! Total: " + productRepository.count());
    }

    private void seedProduct(String code, String name, String description, double price, CategoryEntity category) {
        ProductEntity product = new ProductEntity();
        product.setCode(code);
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);
        product.setImage("/images/default.jpg");
        productRepository.save(product);
        System.out.println("  ✓ Created product: " + code + " - " + name);
    }
}
