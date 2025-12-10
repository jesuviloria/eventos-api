package com.tiquetera.dtos;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueDTO {
    private Long id;
    
    @NotBlank(message = "El nombre del venue es obligatorio")
    @Size(min = 3, max = 100)
    private String nombre;
    
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;
    
    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacidad;
    
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;
}
