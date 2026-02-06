package com.tutorial.tutorial.User.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tutorial.tutorial.User.entity.UserEntity;
import com.tutorial.tutorial.User.entity.UserRole;
import com.tutorial.tutorial.User.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * Register a new user
     */
    public UserEntity registerUser(String username, String email, String password, UserRole role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setActive(true);
        
        return userRepository.save(user);
    }
    
    /**
     * Authenticate user (login)
     */
    public UserEntity authenticateUser(String username, String password) {
        System.out.println("=== UserService.authenticateUser called ===");
        System.out.println("Username: " + username);
        System.out.println("Password provided: " + (password != null && !password.isEmpty()));
        
        Optional<UserEntity> user = userRepository.findByUsername(username);
        
        System.out.println("User found in database: " + user.isPresent());
        
        if (user.isEmpty()) {
            System.out.println("User not found in database");
            return null;
        }
        
        UserEntity foundUser = user.get();
        System.out.println("User active status: " + foundUser.getActive());
        System.out.println("User role: " + foundUser.getRole());
        
        if (!foundUser.getActive()) {
            System.out.println("User is inactive");
            return null;
        }
        
        boolean passwordMatches = passwordEncoder.matches(password, foundUser.getPassword());
        System.out.println("Password matches: " + passwordMatches);
        
        if (passwordMatches) {
            System.out.println("Authentication successful!");
            return foundUser;
        }
        
        System.out.println("Password mismatch");
        return null;
    }
    
    /**
     * Get user by username
     */
    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Get all users
     */
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Get all users with pagination, keyword search, and optional role filter
     */
    public Page<UserEntity> getAllUsersWithPagination(Pageable pageable, String keyword, String role) {
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasRole = role != null && !role.trim().isEmpty();
        
        // Both keyword and role filter
        if (hasKeyword && hasRole) {
            try {
                UserRole userRole = UserRole.valueOf(role.toUpperCase());
                return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseAndRole(
                    keyword, keyword, userRole, pageable);
            } catch (IllegalArgumentException e) {
                // Invalid role, search without role filter
                return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    keyword, keyword, pageable);
            }
        }
        // Only keyword search
        else if (hasKeyword) {
            return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword, keyword, pageable);
        }
        // Only role filter
        else if (hasRole) {
            try {
                UserRole userRole = UserRole.valueOf(role.toUpperCase());
                return userRepository.findByRole(userRole, pageable);
            } catch (IllegalArgumentException e) {
                // Invalid role, return all users
                return userRepository.findAll(pageable);
            }
        }
        // No filters
        return userRepository.findAll(pageable);
    }
    
    /**
     * Get all active users
     */
    public List<UserEntity> getActiveUsers() {
        return userRepository.findByActive(true);
    }
    
    /**
     * Update user
     */
    public UserEntity updateUser(Long id, String email, String username, UserRole role, Boolean active) {
        Optional<UserEntity> user = userRepository.findById(id);
        
        if (user.isEmpty()) {
            return null;
        }
        
        UserEntity existingUser = user.get();
        
        if (!existingUser.getUsername().equals(username) && userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (!existingUser.getEmail().equals(email) && userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        existingUser.setUsername(username);
        existingUser.setEmail(email);
        existingUser.setRole(role);
        existingUser.setActive(active);
        
        return userRepository.save(existingUser);
    }
    
    /**
     * Change password
     */
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        Optional<UserEntity> user = userRepository.findById(id);
        
        if (user.isEmpty()) {
            return false;
        }
        
        UserEntity existingUser = user.get();
        
        if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
            return false;
        }
        
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);
        
        return true;
    }
    
    /**
     * Delete user
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    /**
     * Deactivate user
     */
    public UserEntity deactivateUser(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        
        if (user.isEmpty()) {
            return null;
        }
        
        UserEntity existingUser = user.get();
        existingUser.setActive(false);
        
        return userRepository.save(existingUser);
    }
}
