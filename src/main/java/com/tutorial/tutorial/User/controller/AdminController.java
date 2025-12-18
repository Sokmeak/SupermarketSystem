package com.tutorial.tutorial.User.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.tutorial.User.dto.CreateUserRequest;
import com.tutorial.tutorial.User.dto.LoginRequest;
import com.tutorial.tutorial.User.dto.LoginResponse;
import com.tutorial.tutorial.User.dto.UpdateUserRequest;
import com.tutorial.tutorial.User.dto.UserDTO;
import com.tutorial.tutorial.User.entity.UserEntity;
import com.tutorial.tutorial.User.entity.UserRole;
import com.tutorial.tutorial.User.service.UserService;
import com.tutorial.tutorial.User.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/api")
@Tag(name = "Authentication & User Management", description = "APIs for login and admin user management")
public class AdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping("")
    public String Greeting() {
        return "Hello From Spring Boot API Application!";
    }
    
    @PostMapping({"/login"})
    @Operation(summary = "Admin login", description = "Authenticate admin user and receive JWT token")
    public ResponseEntity<?> handleLogin(@RequestBody LoginRequest request,
                                         HttpSession session) {
        String username = request.getUsername();
        String password = request.getPassword();

        logger.info("=== LOGIN ATTEMPT ===");
        logger.info("Username: {}", username);

        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "username and password are required"));
        }

        UserEntity user = userService.authenticateUser(username, password);

        if (user == null) {
            logger.warn("Authentication failed for user: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        // if (!user.getRole().equals(UserRole.ADMIN)) {
        //     logger.warn("User {} does not have ADMIN role", username);
        //     return ResponseEntity.status(HttpStatus.FORBIDDEN)
        //             .body(Map.of("error", "Insufficient permissions. Admin access required."));
        // }

        logger.info("User {} logged in successfully", username);
        session.setAttribute("admin", user);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().toString());
        
        LoginResponse response = new LoginResponse("Login successful", token, new UserDTO(user));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/session")
    public ResponseEntity<?> getSessionUser(HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("admin");
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not authenticated"));
        }
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/admin/users")
    @Operation(summary = "Get all users", description = "Retrieve list of all users with pagination and optional role filter (admin only)")
    public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy,
                                      @RequestParam(defaultValue = "asc") String sortDir,
                                      @RequestParam(required = false) String role,
                                      HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("admin");
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not authenticated"));
        }

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserEntity> users = userService.getAllUsersWithPagination(pageable, role);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/admin/users")
    @Operation(summary = "Create new user", description = "Create a new user account (admin only)")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request,
                                        HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("admin");
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not authenticated"));
        }

        try {
            if (request.getUsername() == null || request.getEmail() == null || 
                request.getPassword() == null || request.getRole() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "username, email, password, role are required"));
            }
            UserRole role = UserRole.valueOf(request.getRole());
            userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), role);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User created"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/admin/users/{id}")
    @Operation(summary = "Update user", description = "Update user information (admin only)")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UpdateUserRequest request,
                                        HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("admin");
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not authenticated"));
        }

        try {
            if (request.getUsername() == null || request.getEmail() == null || request.getRole() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "username, email, role are required"));
            }

            UserRole role = UserRole.valueOf(request.getRole());
            userService.updateUser(id, request.getEmail(), request.getUsername(), role, 
                                 request.getActive() != null ? request.getActive() : false);
            return ResponseEntity.ok(Map.of("message", "User updated"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/admin/users/{id}/deactivate")
    @Operation(summary = "Deactivate user", description = "Deactivate a user account (admin only)")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id,
                                            HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("admin");
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not authenticated"));
        }

        userService.deactivateUser(id);
        return ResponseEntity.ok(Map.of("message", "User deactivated"));
    }

    @PostMapping("/admin/users/{id}/delete")
    @Operation(summary = "Delete user", description = "Permanently delete a user (admin only)")
    public ResponseEntity<?> deleteUser(@PathVariable Long id,
                                        HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("admin");
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not authenticated"));
        }

        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.removeAttribute("admin");
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
