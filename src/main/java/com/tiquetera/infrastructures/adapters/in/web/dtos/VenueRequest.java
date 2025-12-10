package com.tiquetera.infrastructures.adapters.in.web.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueRequest {
    
    @NotBlank(message = "Venue name is required")
    @Size(min = 3, max = 100)
    private String nombre;
    
    @NotBlank(message = "City is required")
    private String ciudad;
    
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacidad;
    
    @Size(max = 200)
    private String direccion;
}
