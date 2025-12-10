package com.tiquetera.infrastructures.adapters.in.web.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequest {
    
    @NotBlank(message = "Event name is required")
    @Size(min = 3, max = 100)
    private String nombre;
    
    @NotBlank(message = "Description is required")
    @Size(max = 500)
    private String descripcion;
    
    @NotNull(message = "Start date is required")
    @Future
    private LocalDateTime fechaInicio;
    
    @NotNull(message = "End date is required")
    @Future
    private LocalDateTime fechaFin;
    
    @NotNull(message = "Venue is required")
    private Long venueId;
    
    @NotBlank(message = "City is required")
    private String ciudad;
    
    private String categoria;
}
