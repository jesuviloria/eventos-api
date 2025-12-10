package com.tiquetera.infrastructures.adapters.in.web;

import com.tiquetera.domains.models.Event;
import com.tiquetera.domains.ports.in.*;
import com.tiquetera.infrastructures.adapters.in.web.dtos.EventRequest;
import com.tiquetera.infrastructures.adapters.in.web.dtos.EventResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.*;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.*;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.*;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/api/v1/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Events", description = "Event management operations (Secured)")
@SecurityScheme(
    securitySchemeName = "jwt",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
@SecurityRequirement(name = "jwt")
public class EventController {

    @Inject
    CreateEventUseCase createEventUseCase;

    @Inject
    FindEventUseCase findEventUseCase;

    @Inject
    UpdateEventUseCase updateEventUseCase;

    @Inject
    DeleteEventUseCase deleteEventUseCase;

    @POST
    @RolesAllowed({"ADMIN"}) // Solo ADMIN puede crear eventos
    @Operation(summary = "Create a new event (ADMIN only)")
    @APIResponse(responseCode = "201", description = "Event created successfully")
    @APIResponse(responseCode = "400", description = "Invalid data or business rule violation")
    @APIResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    public Response create(@Valid EventRequest request) {
        try {
            Event event = toDomain(request);
            Event created = createEventUseCase.execute(event);
            return Response.status(Response.Status.CREATED)
                .entity(toResponse(created))
                .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }

    @GET
    @PermitAll // Todos pueden ver eventos
    @Operation(summary = "List events with pagination and filters (Public)")
    @APIResponse(responseCode = "200", description = "Events retrieved successfully")
    public Response findAll(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("size") @DefaultValue("10") int size,
        @QueryParam("sort") @DefaultValue("fechaInicio") String sort,
        @QueryParam("ciudad") String ciudad,
        @QueryParam("categoria") String categoria,
        @QueryParam("venueId") Long venueId
    ) {
        List<Event> events;
        if (ciudad != null && !ciudad.isBlank()) {
            events = findEventUseCase.findByCiudad(ciudad, page, size);
        } else if (categoria != null && !categoria.isBlank()) {
            events = findEventUseCase.findByCategoria(categoria, page, size);
        } else if (venueId != null) {
            events = findEventUseCase.findByVenueId(venueId, page, size);
        } else {
            events = findEventUseCase.findAll(page, size, sort);
        }

        List<EventResponse> response = events.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());

        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @PermitAll // Todos pueden ver un evento específico
    @Operation(summary = "Get event by ID (Public)")
    @APIResponse(responseCode = "200", description = "Event found")
    @APIResponse(responseCode = "404", description = "Event not found")
    public Response findById(@PathParam("id") Long id) {
        Event event = findEventUseCase.findById(id);
        return Response.ok(toResponse(event)).build();
    }

    @GET
    @Path("/upcoming")
    @PermitAll
    @Operation(summary = "Get upcoming events (Public)")
    @APIResponse(responseCode = "200", description = "Upcoming events retrieved successfully")
    public Response findUpcomingEvents() {
        List<Event> events = findEventUseCase.findUpcomingEvents();
        List<EventResponse> response = events.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return Response.ok(response).build();
    }

    @GET
    @Path("/search")
    @PermitAll
    @Operation(summary = "Search events by city and category (Public)")
    @APIResponse(responseCode = "200", description = "Events found")
    public Response searchByCiudadAndCategoria(
        @QueryParam("ciudad") @Parameter(description = "City name", required = true) String ciudad,
        @QueryParam("categoria") @Parameter(description = "Category name", required = true) String categoria
    ) {
        List<Event> events = findEventUseCase.findByCiudadAndCategoria(ciudad, categoria);
        List<EventResponse> response = events.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return Response.ok(response).build();
    }

    @GET
    @Path("/range")
    @PermitAll
    @Operation(summary = "Find events in date range (Public)")
    @APIResponse(responseCode = "200", description = "Events in range retrieved")
    public Response findByDateRange(
        @QueryParam("start") @Parameter(description = "Start date (ISO format)", required = true) String start,
        @QueryParam("end") @Parameter(description = "End date (ISO format)", required = true) String end
    ) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        
        List<Event> events = findEventUseCase.findByDateRange(startDate, endDate);
        List<EventResponse> response = events.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return Response.ok(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN"}) // Solo ADMIN puede actualizar
    @Operation(summary = "Update event (ADMIN only)")
    @APIResponse(responseCode = "200", description = "Event updated successfully")
    @APIResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    public Response update(@PathParam("id") Long id, @Valid EventRequest request) {
        Event toUpdate = toDomain(request);
        Event updated = updateEventUseCase.execute(id, toUpdate);
        return Response.ok(toResponse(updated)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"}) // Solo ADMIN puede eliminar
    @Operation(summary = "Delete event (ADMIN only)")
    @APIResponse(responseCode = "204", description = "Event deleted successfully")
    @APIResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    public Response delete(@PathParam("id") Long id) {
        deleteEventUseCase.execute(id);
        return Response.noContent().build();
    }

    private Event toDomain(EventRequest request) {
        return Event.builder()
            .nombre(request.getNombre())
            .descripcion(request.getDescripcion())
            .fechaInicio(request.getFechaInicio())
            .fechaFin(request.getFechaFin())
            .venueId(request.getVenueId())
            .ciudad(request.getCiudad())
            .categoria(request.getCategoria())
            .build();
    }

    private EventResponse toResponse(Event event) {
        return EventResponse.builder()
            .id(event.getId())
            .nombre(event.getNombre())
            .descripcion(event.getDescripcion())
            .fechaInicio(event.getFechaInicio())
            .fechaFin(event.getFechaFin())
            .venueId(event.getVenueId())
            .ciudad(event.getCiudad())
            .categoria(event.getCategoria())
            .build();
    }
}
