package com.tiquetera.infrastructures.adapters.in.web.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueResponse {
    private Long id;
    private String nombre;
    private String ciudad;
    private Integer capacidad;
    private String direccion;
}
