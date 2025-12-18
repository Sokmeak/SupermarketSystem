package com.tutorial.tutorial.User.entity;

public enum UserRole {
    ADMIN("Administrator"),
    STAFF("Staff"),
    VIEWER("Viewer");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
