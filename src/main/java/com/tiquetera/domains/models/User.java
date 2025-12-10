package com.tiquetera.domains.models;

import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain model for User - Pure business logic
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    private Long id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private Boolean enabled;
    
    @Builder.Default
    private Set<String> roles = new HashSet<>();
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Business validation
    public void validate() {
        validateUsername();
        validateEmail();
        validatePassword();
        validateFullName();
    }
    
    private void validateUsername() {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Username must be between 3 and 50 characters");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Username can only contain letters, numbers and underscore");
        }
    }
    
    private void validateEmail() {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    private void validatePassword() {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
    }
    
    private void validateFullName() {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required");
        }
    }
    
    public boolean hasRole(String role) {
        return roles.contains(role);
    }
    
    public boolean isAdmin() {
        return roles.contains("ADMIN");
    }
}