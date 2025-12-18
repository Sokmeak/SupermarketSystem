package com.tutorial.tutorial.User.dto;

import com.tutorial.tutorial.User.entity.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreateUserRequest {
    
    @Schema(description = "Username", example = "john_doe", required = true)
    private String username;
    
    @Schema(description = "Email address", example = "john@example.com", required = true)
    private String email;
    
    @Schema(description = "Password", example = "securePass123", required = true)
    private String password;
    
    @Schema(description = "User role", example = "USER", required = true, allowableValues = {"ADMIN", "USER"})
    private String role;
    
    public CreateUserRequest() {
    }
    
    public CreateUserRequest(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}
