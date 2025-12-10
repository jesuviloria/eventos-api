package com.tiquetera.infrastructures.adapters.in.web;

import com.tiquetera.domains.models.Event;
import com.tiquetera.domains.ports.in.*;
import com.tiquetera.infrastructures.adapters.in.web.dtos.EventRequest;
import com.tiquetera.infrastructures.adapters.in.web.dtos.EventResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.*;
import org.eclipse.microprofile.openapi.annotations.media.*;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.*;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/api/v1/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Events", description = "Event management operations (Hexagonal Architecture)")
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
    @Operation(summary = "Create a new event")
    @APIResponse(responseCode = "201", description = "Event created successfully")
    @APIResponse(responseCode = "400", description = "Invalid data or business rule violation")
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
    @Operation(summary = "List events with pagination and filters")
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
    @Operation(summary = "Get event by ID")
    @APIResponse(responseCode = "200", description = "Event found")
    @APIResponse(responseCode = "404", description = "Event not found")
    public Response findById(@PathParam("id") Long id) {
        Event event = findEventUseCase.findById(id);
        return Response.ok(toResponse(event)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update event")
    @APIResponse(responseCode = "200", description = "Event updated successfully")
    public Response update(@PathParam("id") Long id, @Valid EventRequest request) {
        Event toUpdate = toDomain(request);
        Event updated = updateEventUseCase.execute(id, toUpdate);
        return Response.ok(toResponse(updated)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete event")
    @APIResponse(responseCode = "204", description = "Event deleted successfully")
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
