package com.tiquetera.infrastructures.configs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * RFC 7807 Problem Details for HTTP APIs
 * https://tools.ietf.org/html/rfc7807
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetail {
    
    /**
     * A URI reference that identifies the problem type
     */
    private String type;
    
    /**
     * A short, human-readable summary of the problem type
     */
    private String title;
    
    /**
     * The HTTP status code
     */
    private Integer status;
    
    /**
     * A human-readable explanation specific to this occurrence
     */
    private String detail;
    
    /**
     * A URI reference that identifies the specific occurrence
     */
    private String instance;
    
    /**
     * Timestamp when the error occurred
     */
    private LocalDateTime timestamp;
    
    /**
     * Trace ID for debugging
     */
    private String traceId;
    
    /**
     * Additional properties specific to the problem
     */
    private Map<String, Object> extensions;
    
    // Factory methods for common errors
    
    public static ProblemDetail badRequest(String detail, String traceId) {
        return ProblemDetail.builder()
            .type("https://tiquetera.com/problems/bad-request")
            .title("Bad Request")
            .status(400)
            .detail(detail)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .build();
    }
    
    public static ProblemDetail unauthorized(String detail, String traceId) {
        return ProblemDetail.builder()
            .type("https://tiquetera.com/problems/unauthorized")
            .title("Unauthorized")
            .status(401)
            .detail(detail)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .build();
    }
    
    public static ProblemDetail forbidden(String detail, String traceId) {
        return ProblemDetail.builder()
            .type("https://tiquetera.com/problems/forbidden")
            .title("Forbidden")
            .status(403)
            .detail(detail)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .build();
    }
    
    public static ProblemDetail notFound(String detail, String traceId) {
        return ProblemDetail.builder()
            .type("https://tiquetera.com/problems/not-found")
            .title("Not Found")
            .status(404)
            .detail(detail)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .build();
    }
    
    public static ProblemDetail conflict(String detail, String traceId) {
        return ProblemDetail.builder()
            .type("https://tiquetera.com/problems/conflict")
            .title("Conflict")
            .status(409)
            .detail(detail)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .build();
    }
    
    public static ProblemDetail internalServerError(String detail, String traceId) {
        return ProblemDetail.builder()
            .type("https://tiquetera.com/problems/internal-server-error")
            .title("Internal Server Error")
            .status(500)
            .detail(detail)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .build();
    }
}
