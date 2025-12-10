package com.tiquetera.domains.models;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Domain model for Event - Pure business logic, no framework dependencies
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Long venueId;
    private String ciudad;
    private String categoria;
    
    // Business validation logic
    public void validate() {
        validateName();
        validateDates();
        validateVenue();
    }
    
    private void validateName() {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Event name is required");
        }
        if (nombre.length() < 3 || nombre.length() > 100) {
            throw new IllegalArgumentException("Event name must be between 3 and 100 characters");
        }
    }
    
    private void validateDates() {
        if (fechaInicio == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        if (fechaFin == null) {
            throw new IllegalArgumentException("End date is required");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        if (fechaInicio.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start date must be in the future");
        }
    }
    
    private void validateVenue() {
        if (venueId == null) {
            throw new IllegalArgumentException("Venue is required");
        }
    }
    
    public boolean isUpcoming() {
        return fechaInicio.isAfter(LocalDateTime.now());
    }
    
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(fechaInicio) && now.isBefore(fechaFin);
    }
}
