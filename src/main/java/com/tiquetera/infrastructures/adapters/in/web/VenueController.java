package com.tiquetera.infrastructures.adapters.in.web;

import com.tiquetera.domains.models.Venue;
import com.tiquetera.domains.ports.in.CreateVenueUseCase;
import com.tiquetera.domains.ports.in.DeleteVenueUseCase;
import com.tiquetera.domains.ports.in.FindVenueUseCase;
import com.tiquetera.domains.ports.in.UpdateVenueUseCase;
import com.tiquetera.infrastructures.adapters.in.web.dtos.VenueRequest;
import com.tiquetera.infrastructures.adapters.in.web.dtos.VenueResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/api/v1/venues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Venues", description = "Venue management operations (Secured)")
@SecurityScheme(
    securitySchemeName = "jwt",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
@SecurityRequirement(name = "jwt")
public class VenueController {

    @Inject
    CreateVenueUseCase createVenueUseCase;

    @Inject
    FindVenueUseCase findVenueUseCase;

    @Inject
    UpdateVenueUseCase updateVenueUseCase;

    @Inject
    DeleteVenueUseCase deleteVenueUseCase;

    @POST
    @RolesAllowed({"ADMIN"})
    @Operation(
        summary = "Create a new venue (ADMIN only)",
        description = "Creates a new venue in the system. Only users with ADMIN role can perform this operation."
    )
    @APIResponse(
        responseCode = "201",
        description = "Venue created successfully",
        content = @Content(schema = @Schema(implementation = VenueResponse.class))
    )
    @APIResponse(responseCode = "400", description = "Invalid data or business rule violation")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    @APIResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    @APIResponse(responseCode = "409", description = "Conflict - Venue with same name already exists")
    public Response create(@Valid VenueRequest request) {
        try {
            Venue venue = toDomain(request);
            Venue created = createVenueUseCase.execute(venue);
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
    @PermitAll
    @Operation(
        summary = "List venues with pagination and filters (Public)",
        description = "Returns a paginated list of venues. This endpoint is public and doesn't require authentication."
    )
    @APIResponse(
        responseCode = "200",
        description = "Venues retrieved successfully",
        content = @Content(schema = @Schema(implementation = VenueResponse.class))
    )
    public Response findAll(
        @QueryParam("page")
        @DefaultValue("0")
        @Parameter(description = "Page number (0-based)", example = "0") 
        int page,

        @QueryParam("size")
        @DefaultValue("10")
        @Parameter(description = "Number of items per page", example = "10") 
        int size,

        @QueryParam("sort")
        @DefaultValue("nombre")
        @Parameter(description = "Sort field (nombre, ciudad, capacidad)", example = "nombre") 
        String sort,

        @QueryParam("ciudad")
        @Parameter(description = "Filter by city name", example = "Bogotá") 
        String ciudad,

        @QueryParam("capacidadMinima")
        @Parameter(description = "Filter by minimum capacity", example = "5000") 
        Integer capacidadMinima
    ) {
        List<Venue> venues;
        
        if (ciudad != null && !ciudad.isBlank()) {
            venues = findVenueUseCase.findByCiudad(ciudad, page, size);
        } else if (capacidadMinima != null) {
            venues = findVenueUseCase.findByCapacidadMinima(capacidadMinima, page, size);
        } else {
            venues = findVenueUseCase.findAll(page, size, sort);
        }

        List<VenueResponse> response = venues.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());

        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @PermitAll
    @Operation(
        summary = "Get venue by ID (Public)",
        description = "Returns detailed information about a specific venue. Public endpoint."
    )
    @APIResponse(
        responseCode = "200",
        description = "Venue found",
        content = @Content(schema = @Schema(implementation = VenueResponse.class))
    )
    @APIResponse(responseCode = "404", description = "Venue not found")
    public Response findById(
        @PathParam("id")
        @Parameter(description = "Venue ID", required = true, example = "1") 
        Long id
    ) {
        Venue venue = findVenueUseCase.findById(id);
        return Response.ok(toResponse(venue)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @Operation(
        summary = "Update venue (ADMIN only)",
        description = "Updates an existing venue. Only users with ADMIN role can perform this operation."
    )
    @APIResponse(
        responseCode = "200",
        description = "Venue updated successfully",
        content = @Content(schema = @Schema(implementation = VenueResponse.class))
    )
    @APIResponse(responseCode = "400", description = "Invalid data")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    @APIResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    @APIResponse(responseCode = "404", description = "Venue not found")
    @APIResponse(responseCode = "409", description = "Conflict - Another venue with same name exists")
    public Response update(
        @PathParam("id")
        @Parameter(description = "Venue ID", required = true, example = "1") 
        Long id,
        @Valid VenueRequest request
    ) {
        Venue toUpdate = toDomain(request);
        Venue updated = updateVenueUseCase.execute(id, toUpdate);
        return Response.ok(toResponse(updated)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @Operation(
        summary = "Delete venue (ADMIN only)",
        description = "Deletes a venue from the system. Only users with ADMIN role can perform this operation. " +
                     "This will also delete all events associated with this venue (cascade delete)."
    )
    @APIResponse(responseCode = "204", description = "Venue deleted successfully")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    @APIResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    @APIResponse(responseCode = "404", description = "Venue not found")
    public Response delete(
        @PathParam("id")
        @Parameter(description = "Venue ID", required = true, example = "1") 
        Long id
    ) {
        deleteVenueUseCase.execute(id);
        return Response.noContent().build();
    }

    // Mapper methods
    
    private Venue toDomain(VenueRequest request) {
        return Venue.builder()
            .nombre(request.getNombre())
            .ciudad(request.getCiudad())
            .capacidad(request.getCapacidad())
            .direccion(request.getDireccion())
            .build();
    }

    private VenueResponse toResponse(Venue venue) {
        return VenueResponse.builder()
            .id(venue.getId())
            .nombre(venue.getNombre())
            .ciudad(venue.getCiudad())
            .capacidad(venue.getCapacidad())
            .direccion(venue.getDireccion())
            .build();
    }
}