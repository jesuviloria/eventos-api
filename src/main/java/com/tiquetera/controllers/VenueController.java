package com.tiquetera.controllers;

import com.tiquetera.dtos.VenueDTO;
import com.tiquetera.services.VenueService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.*;
import org.eclipse.microprofile.openapi.annotations.media.*;
import org.eclipse.microprofile.openapi.annotations.parameters.*;
import org.eclipse.microprofile.openapi.annotations.responses.*;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Path("/api/v1/venues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Venues", description = "Venue management operations")
public class VenueController {
    
    @Inject
    VenueService venueService;
    
    @POST
    @Operation(summary = "Create a new venue")
    @APIResponse(responseCode = "201", description = "Venue created successfully")
    @APIResponse(responseCode = "400", description = "Invalid data or duplicate venue")
    public Response create(@Valid VenueDTO venue) {
        try {
            VenueDTO created = venueService.create(venue);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Operation(summary = "List all venues with pagination and filters")
    @APIResponse(responseCode = "200", description = "Venues retrieved successfully")
    public Response findAll(
        @QueryParam("page") @DefaultValue("0") 
        @Parameter(description = "Page number") 
        int page,
        
        @QueryParam("size") @DefaultValue("10") 
        @Parameter(description = "Page size") 
        int size,
        
        @QueryParam("sort") @DefaultValue("nombre") 
        @Parameter(description = "Sort field") 
        String sort,
        
        @QueryParam("ciudad") 
        @Parameter(description = "Filter by city") 
        String ciudad,
        
        @QueryParam("capacidadMinima") 
        @Parameter(description = "Filter by minimum capacity") 
        Integer capacidadMinima
    ) {
        List<VenueDTO> venues;
        
        if (ciudad != null && !ciudad.isBlank()) {
            venues = venueService.findByCiudad(ciudad, page, size);
        } else if (capacidadMinima != null) {
            venues = venueService.findByCapacidadMinima(capacidadMinima, page, size);
        } else {
            venues = venueService.findAll(page, size, sort);
        }
        
        return Response.ok(venues).build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get venue by ID")
    @APIResponse(responseCode = "200", description = "Venue found")
    @APIResponse(responseCode = "404", description = "Venue not found")
    public Response findById(@PathParam("id") Long id) {
        try {
            VenueDTO venue = venueService.findById(id);
            return Response.ok(venue).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Venue not found"))
                .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    @Operation(summary = "Update existing venue")
    @APIResponse(responseCode = "200", description = "Venue updated successfully")
    @APIResponse(responseCode = "404", description = "Venue not found")
    public Response update(@PathParam("id") Long id, @Valid VenueDTO venue) {
        try {
            VenueDTO updated = venueService.update(id, venue);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete venue")
    @APIResponse(responseCode = "204", description = "Venue deleted successfully")
    @APIResponse(responseCode = "404", description = "Venue not found")
    public Response delete(@PathParam("id") Long id) {
        try {
            venueService.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Venue not found"))
                .build();
        }
    }
}
