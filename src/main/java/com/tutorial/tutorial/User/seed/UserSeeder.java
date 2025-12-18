package com.tutorial.tutorial.User.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tutorial.tutorial.User.entity.UserEntity;
import com.tutorial.tutorial.User.entity.UserRole;
import com.tutorial.tutorial.User.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
public class UserSeeder implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    // dependency for password encoding
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    
    @Override
    public void run(String... args) throws Exception {
        // Check if users already exist
        if (userRepository.count() > 0) {
            System.out.println("Users already seeded. Skipping initialization.");
            return;
        }
        
        System.out.println("Seeding initial users...");
        
        // Create admin user
        createUser("admin", "admin@supermarket.com", "admin123", UserRole.ADMIN);
        
        // Create staff users
        createUser("staff1", "staff1@supermarket.com", "staff123", UserRole.STAFF);
        createUser("staff2", "staff2@supermarket.com", "staff123", UserRole.STAFF);
        
        
        System.out.println("Users seeded successfully!");
    }
    
    private void createUser(String username, String email, String password, UserRole role) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setActive(true);
        
        userRepository.save(user);
        System.out.println("  ✓ Created user: " + username + " (" + role.getDisplayName() + ")");
    }
}
