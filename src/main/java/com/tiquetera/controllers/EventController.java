package com.tiquetera.controllers;

import com.tiquetera.dtos.EventDTO;
import com.tiquetera.services.EventService;
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

@Path("/api/v1/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Events", description = "Event management operations")
public class EventController {
    
    @Inject
    EventService eventService;
    
    @POST
    @Operation(summary = "Create a new event")
    @APIResponse(responseCode = "201", description = "Event created successfully")
    @APIResponse(responseCode = "400", description = "Invalid data or duplicate event")
    public Response create(@Valid EventDTO event) {
        try {
            EventDTO created = eventService.create(event);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Operation(summary = "List all events with pagination and filters")
    @APIResponse(responseCode = "200", description = "Events retrieved successfully")
    public Response findAll(
        @QueryParam("page") @DefaultValue("0") 
        @Parameter(description = "Page number") 
        int page,
        
        @QueryParam("size") @DefaultValue("10") 
        @Parameter(description = "Page size") 
        int size,
        
        @QueryParam("sort") @DefaultValue("fechaInicio") 
        @Parameter(description = "Sort field") 
        String sort,
        
        @QueryParam("ciudad") 
        @Parameter(description = "Filter by city") 
        String ciudad,
        
        @QueryParam("categoria") 
        @Parameter(description = "Filter by category") 
        String categoria,
        
        @QueryParam("venueId") 
        @Parameter(description = "Filter by venue ID") 
        Long venueId
    ) {
        List<EventDTO> events;
        
        if (ciudad != null && !ciudad.isBlank()) {
            events = eventService.findByCiudad(ciudad, page, size);
        } else if (categoria != null && !categoria.isBlank()) {
            events = eventService.findByCategoria(categoria, page, size);
        } else if (venueId != null) {
            events = eventService.findByVenueId(venueId, page, size);
        } else {
            events = eventService.findAll(page, size, sort);
        }
        
        return Response.ok(events).build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get event by ID")
    @APIResponse(responseCode = "200", description = "Event found")
    @APIResponse(responseCode = "404", description = "Event not found")
    public Response findById(@PathParam("id") Long id) {
        try {
            EventDTO event = eventService.findById(id);
            return Response.ok(event).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Event not found"))
                .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    @Operation(summary = "Update existing event")
    @APIResponse(responseCode = "200", description = "Event updated successfully")
    @APIResponse(responseCode = "404", description = "Event not found")
    public Response update(@PathParam("id") Long id, @Valid EventDTO event) {
        try {
            EventDTO updated = eventService.update(id, event);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete event")
    @APIResponse(responseCode = "204", description = "Event deleted successfully")
    @APIResponse(responseCode = "404", description = "Event not found")
    public Response delete(@PathParam("id") Long id) {
        try {
            eventService.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Event not found"))
                .build();
        }
    }
}