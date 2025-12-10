package com.tiquetera.infrastructures.configs;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Global exception handler implementing RFC 7807
 */
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Exception exception) {
        String traceId = generateTraceId();
        log.error("Exception caught [traceId={}]: {}", traceId, exception.getMessage(), exception);

        // Handle specific exceptions
        if (exception instanceof NotFoundException) {
            return handleNotFoundException((NotFoundException) exception, traceId);
        }

        if (exception instanceof NotAuthorizedException) {
            return handleNotAuthorizedException((NotAuthorizedException) exception, traceId);
        }

        if (exception instanceof ForbiddenException) {
            return handleForbiddenException((ForbiddenException) exception, traceId);
        }

        if (exception instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) exception, traceId);
        }

        if (exception instanceof IllegalArgumentException) {
            return handleIllegalArgumentException((IllegalArgumentException) exception, traceId);
        }

        if (exception instanceof jakarta.persistence.EntityNotFoundException) {
            return handleEntityNotFoundException(exception, traceId);
        }

        // Handle generic exceptions
        return handleGenericException(exception, traceId);
    }

    private Response handleNotFoundException(NotFoundException exception, String traceId) {
        ProblemDetail problem = ProblemDetail.notFound(
            exception.getMessage() != null ? exception.getMessage() : "Resource not found",
            traceId
        );
        return Response.status(Response.Status.NOT_FOUND)
            .entity(problem)
            .type("application/problem+json")
            .build();
    }

    private Response handleNotAuthorizedException(NotAuthorizedException exception, String traceId) {
        ProblemDetail problem = ProblemDetail.unauthorized(
            "Authentication is required to access this resource",
            traceId
        );
        return Response.status(Response.Status.UNAUTHORIZED)
            .entity(problem)
            .type("application/problem+json")
            .header("WWW-Authenticate", "Bearer")
            .build();
    }

    private Response handleForbiddenException(ForbiddenException exception, String traceId) {
        ProblemDetail problem = ProblemDetail.forbidden(
            "You don't have permission to access this resource",
            traceId
        );
        return Response.status(Response.Status.FORBIDDEN)
            .entity(problem)
            .type("application/problem+json")
            .build();
    }

    private Response handleConstraintViolationException(ConstraintViolationException exception, String traceId) {
        String violations = exception.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining(", "));

        ProblemDetail problem = ProblemDetail.badRequest(
            violations.isEmpty() ? "Validation failed" : violations,
            traceId
        );
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(problem)
            .type("application/problem+json")
            .build();
    }

    private Response handleIllegalArgumentException(IllegalArgumentException exception, String traceId) {
        ProblemDetail problem = ProblemDetail.conflict(
            exception.getMessage(),
            traceId
        );
        return Response.status(Response.Status.CONFLICT)
            .entity(problem)
            .type("application/problem+json")
            .build();
    }

    private Response handleEntityNotFoundException(Exception exception, String traceId) {
        ProblemDetail problem = ProblemDetail.notFound(
            "The requested entity was not found in the database",
            traceId
        );
        return Response.status(Response.Status.NOT_FOUND)
            .entity(problem)
            .type("application/problem+json")
            .build();
    }

    private Response handleGenericException(Exception exception, String traceId) {
        log.error("Unhandled exception [traceId={}]: {}", traceId, exception.getMessage(), exception);

        ProblemDetail problem = ProblemDetail.internalServerError(
            "An unexpected error occurred. Please contact support with trace ID: " + traceId,
            traceId
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(problem)
            .type("application/problem+json")
            .build();
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
