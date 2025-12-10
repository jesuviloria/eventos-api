package com.tiquetera.domains.models;

import lombok.*;

/**
 * Domain model for Venue - Pure business logic, no framework dependencies
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {
    
    private Long id;
    private String nombre;
    private String ciudad;
    private Integer capacidad;
    private String direccion;
    
    // Business validation logic
    public void validate() {
        validateName();
        validateCapacity();
        validateCity();
    }
    
    private void validateName() {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Venue name is required");
        }
        if (nombre.length() < 3 || nombre.length() > 100) {
            throw new IllegalArgumentException("Venue name must be between 3 and 100 characters");
        }
    }
    
    private void validateCapacity() {
        if (capacidad == null || capacidad < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1");
        }
    }
    
    private void validateCity() {
        if (ciudad == null || ciudad.isBlank()) {
            throw new IllegalArgumentException("City is required");
        }
    }
    
    public boolean canHost(int attendees) {
        return attendees <= capacidad;
    }
}
