package com.tiquetera.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {
    
    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof NotFoundException) {
            return Response.status(404)
                .entity(createErrorResponse("Resource not found", exception.getMessage(), 404))
                .build();
        }
        
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) exception;
            String violations = cve.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            
            return Response.status(400)
                .entity(createErrorResponse("Validation failed", violations, 400))
                .build();
        }
        
        if (exception instanceof IllegalArgumentException) {
            return Response.status(409)
                .entity(createErrorResponse("Business rule violation", exception.getMessage(), 409))
                .build();
        }
        
        return Response.status(500)
            .entity(createErrorResponse("Internal server error", "An unexpected error occurred", 500))
            .build();
    }
    
    private Map<String, Object> createErrorResponse(String error, String message, int status) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("error", error);
        errorMap.put("message", message);
        errorMap.put("status", status);
        errorMap.put("timestamp", LocalDateTime.now());
        return errorMap;
    }
}
