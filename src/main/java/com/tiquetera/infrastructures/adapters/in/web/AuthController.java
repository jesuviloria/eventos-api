package com.tiquetera.infrastructures.adapters.in.web;

import com.tiquetera.domains.models.User;
import com.tiquetera.domains.ports.in.LoginUserUseCase;
import com.tiquetera.domains.ports.in.RegisterUserUseCase;
import com.tiquetera.infrastructures.adapters.in.web.dtos.*;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.*;
import org.eclipse.microprofile.openapi.annotations.media.*;
import org.eclipse.microprofile.openapi.annotations.responses.*;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Map;

@Path("/api/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {

    @Inject
    RegisterUserUseCase registerUserUseCase;

    @Inject
    LoginUserUseCase loginUserUseCase;

    @POST
    @Path("/register")
    @PermitAll
    @Operation(summary = "Register a new user")
    @APIResponse(
        responseCode = "201",
        description = "User registered successfully",
        content = @Content(schema = @Schema(implementation = AuthResponse.class))
    )
    @APIResponse(responseCode = "400", description = "Invalid data or user already exists")
    public Response register(@Valid RegisterRequest request) {
        try {
            User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .build();

            User created = registerUserUseCase.execute(user);

            // Generar token automáticamente después del registro
            String token = loginUserUseCase.execute(created.getUsername(), request.getPassword());

            AuthResponse response = AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .username(created.getUsername())
                .email(created.getEmail())
                .build();

            return Response.status(Response.Status.CREATED).entity(response).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }

    @POST
    @Path("/login")
    @PermitAll
    @Operation(summary = "Login with username and password")
    @APIResponse(
        responseCode = "200",
        description = "Login successful",
        content = @Content(schema = @Schema(implementation = AuthResponse.class))
    )
    @APIResponse(responseCode = "401", description = "Invalid credentials")
    public Response login(@Valid LoginRequest request) {
        try {
            String token = loginUserUseCase.execute(request.getUsername(), request.getPassword());

            AuthResponse response = AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .username(request.getUsername())
                .build();

            return Response.ok(response).build();

        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of("error", "Invalid credentials"))
                .build();
        }
    }
}
