package com.tutorial.tutorial.User.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginResponse {
    
    @Schema(description = "Success message", example = "Login successful")
    private String message;
    
    @Schema(description = "JWT token for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "User information without password")
    private UserDTO user;
    
    public LoginResponse() {
    }
    
    public LoginResponse(String message, String token, UserDTO user) {
        this.message = message;
        this.token = token;
        this.user = user;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public UserDTO getUser() {
        return user;
    }
    
    public void setUser(UserDTO user) {
        this.user = user;
    }
}
