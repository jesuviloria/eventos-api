package com.tiquetera.infrastructures.adapters.in.web.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Long venueId;
    private String venueNombre;
    private String ciudad;
    private String categoria;
}
