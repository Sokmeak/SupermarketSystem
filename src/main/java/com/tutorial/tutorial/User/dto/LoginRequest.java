package com.tutorial.tutorial.User.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginRequest {
    
    @Schema(description = "Username", example = "admin", required = true)
    private String username;
    
    @Schema(description = "Password", example = "admin123", required = true)
    private String password;
    
    public LoginRequest() {
    }
    
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
