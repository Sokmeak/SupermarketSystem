package com.tutorial.tutorial.User.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateUserRequest {
    
    @Schema(description = "Username", example = "john_doe_updated", required = true)
    private String username;
    
    @Schema(description = "Email address", example = "john.updated@example.com", required = true)
    private String email;
    
    @Schema(description = "User role", example = "USER", required = true, allowableValues = {"ADMIN", "USER"})
    private String role;
    
    @Schema(description = "Account active status", example = "true")
    private Boolean active;
    
    public UpdateUserRequest() {
    }
    
    public UpdateUserRequest(String username, String email, String role, Boolean active) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.active = active;
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
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
}
