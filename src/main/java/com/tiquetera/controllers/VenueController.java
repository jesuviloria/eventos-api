package com.tiquetera.controllers;

import com.tiquetera.dtos.VenueDTO;
import com.tiquetera.services.VenueService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.*;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.*;
import org.eclipse.microprofile.openapi.annotations.parameters.*;
import org.eclipse.microprofile.openapi.annotations.responses.*;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Path("/api/v1/venues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Venues", description = "Gestión de lugares/sedes para eventos")
public class VenueController {
    
    @Inject
    VenueService venueService;
    
    @POST
    @Operation(
        summary = "Crear un nuevo venue",
        description = "Crea un nuevo lugar/sede donde se realizarán eventos"
    )
    @APIResponse(
        responseCode = "201",
        description = "Venue creado exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = VenueDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Datos inválidos o venue duplicado"
    )
    public Response create(@Valid VenueDTO venue) {
        try {
            VenueDTO created = venueService.create(venue);
            return Response
                .status(Response.Status.CREATED)
                .entity(created)
                .build();
        } catch (IllegalArgumentException e) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Operation(
        summary = "Listar todos los venues",
        description = "Obtiene el listado completo de lugares/sedes disponibles"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de venues obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = VenueDTO.class, type = SchemaType.ARRAY)
        )
    )
    public Response findAll(
        @QueryParam("ciudad") 
        @Parameter(description = "Filtrar por ciudad") 
        String ciudad,
        
        @QueryParam("capacidadMinima") 
        @Parameter(description = "Filtrar por capacidad mínima") 
        Integer capacidadMinima
    ) {
        List<VenueDTO> venues;
        
        if (ciudad != null && !ciudad.isBlank()) {
            venues = venueService.findByCiudad(ciudad);
        } else if (capacidadMinima != null) {
            venues = venueService.findByCapacidadMinima(capacidadMinima);
        } else {
            venues = venueService.findAll();
        }
        
        return Response.ok(venues).build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(
        summary = "Obtener venue por ID",
        description = "Busca un venue específico por su identificador"
    )
    @APIResponse(
        responseCode = "200",
        description = "Venue encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = VenueDTO.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Venue no encontrado"
    )
    public Response findById(
        @PathParam("id") 
        @Parameter(description = "ID del venue", required = true) 
        Long id
    ) {
        return venueService.findById(id)
            .map(venue -> Response.ok(venue).build())
            .orElse(Response
                .status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Venue no encontrado"))
                .build()
            );
    }
    
    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Actualizar venue existente",
        description = "Modifica los datos de un venue existente"
    )
    @APIResponse(
        responseCode = "200",
        description = "Venue actualizado exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = VenueDTO.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Venue no encontrado"
    )
    @APIResponse(
        responseCode = "400",
        description = "Datos inválidos"
    )
    public Response update(
        @PathParam("id") 
        @Parameter(description = "ID del venue", required = true) 
        Long id,
        
        @Valid VenueDTO venue
    ) {
        try {
            VenueDTO updated = venueService.update(id, venue);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(
        summary = "Eliminar venue",
        description = "Elimina un venue del sistema"
    )
    @APIResponse(
        responseCode = "204",
        description = "Venue eliminado exitosamente"
    )
    @APIResponse(
        responseCode = "404",
        description = "Venue no encontrado"
    )
    public Response delete(
        @PathParam("id") 
        @Parameter(description = "ID del venue", required = true) 
        Long id
    ) {
        try {
            venueService.delete(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
}
