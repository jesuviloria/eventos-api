package com.tiquetera.controllers;

import com.tiquetera.dtos.EventDTO;
import com.tiquetera.services.EventService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.*;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.*;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/v1/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Events", description = "Gestión de eventos")
public class EventController {

    @Inject
    EventService eventService;

    @POST
    @Operation(summary = "Crear un nuevo evento")
    @APIResponse(responseCode = "201", description = "Evento creado exitosamente")
    @APIResponse(responseCode = "400", description = "Datos inválidos")
    public Response create(@Valid EventDTO event) {
        EventDTO created = eventService.create(event);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Operation(summary = "Listar todos los eventos")
    public Response findAll() {
        return Response.ok(eventService.findAll()).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener evento por ID")
    @APIResponse(responseCode = "200", description = "Evento encontrado")
    @APIResponse(responseCode = "404", description = "Evento no encontrado")
    public Response findById(@PathParam("id") Long id) {
        return eventService.findById(id)
                .map(event -> Response.ok(event).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar evento existente")
    public Response update(@PathParam("id") Long id, @Valid EventDTO event) {
        try {
            EventDTO updated = eventService.update(id, event);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar evento")
    @APIResponse(responseCode = "204", description = "Evento eliminado")
    public Response delete(@PathParam("id") Long id) {
        eventService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Operation(summary = "Listar todos los eventos con filtros opcionales")
    @APIResponse(responseCode = "200", description = "Lista de eventos obtenida exitosamente")
    public Response findAll(
            @QueryParam("ciudad") @Parameter(description = "Filtrar por ciudad") String ciudad,

            @QueryParam("categoria") @Parameter(description = "Filtrar por categoría") String categoria,

            @QueryParam("venueId") @Parameter(description = "Filtrar por venue") Long venueId) {
        List<EventDTO> events;

        if (ciudad != null && !ciudad.isBlank()) {
            events = eventService.findByCiudad(ciudad);
        } else if (categoria != null && !categoria.isBlank()) {
            events = eventService.findByCategoria(categoria);
        } else if (venueId != null) {
            events = eventService.findByVenueId(venueId);
        } else {
            events = eventService.findAll();
        }

        return Response.ok(events).build();
    }
}