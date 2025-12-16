package com.tutorial.tutorial.Product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tutorial.tutorial.Category.entity.CategoryEntity;
import com.tutorial.tutorial.Category.repository.CategoryRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if categories already exist
        if (categoryRepository.count() > 0) {
            System.out.println("Categories already seeded. Skipping initialization.");
            return;
        }

        System.out.println("Seeding initial categories...");

        // Electronics Category and Subcategories
        seedCategory("Electronics", "Electronic devices and gadgets");
        // Plastics Category and Subcategories
        seedCategory("Plastics", "Plastic products and materials");
        // Textiles Category
        seedCategory("Textiles", "Fabric, clothing, and textile-related items");
        // Chemicals Category
        seedCategory("Chemicals", "Industrial chemicals and cleaning agents");
        // Machinery/Capital Goods Category
        seedCategory("Machinery & Capital Goods", "Large equipment, vehicles, and industrial tools");
        // General Categories
        seedCategory("Office Supplies", "Stationery, office equipment, and workplace essentials");
        seedCategory("Food & Beverages", "Food products, drinks, and consumables");
        seedCategory("Healthcare & Medical", "Medical devices, pharmaceuticals, and healthcare products");
        seedCategory("Automotive", "Car parts, automotive accessories, and vehicle maintenance items");
        seedCategory("Sports & Recreation", "Sports equipment, fitness gear, and recreational items");
        seedCategory("Furniture", "Office furniture, home furniture, and fixtures");

        System.out.println("Categories seeded successfully!");
    }

    private void seedCategory(String name, String description) {
        CategoryEntity category = new CategoryEntity(name, description);
        categoryRepository.save(category);
        System.out.println("  ✓ Created category: " + name);
    }
}
