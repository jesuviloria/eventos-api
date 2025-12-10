package com.tiquetera.infrastructures.configs;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class JwtService {

    @ConfigProperty(name = "jwt.issuer")
    String issuer;

    @ConfigProperty(name = "jwt.duration", defaultValue = "3600")
    Long duration;

    /**
     * Generate JWT token for user
     */
    public String generateToken(String username, Set<String> roles) {
        return Jwt.issuer(issuer)
            .upn(username)
            .groups(roles)
            .expiresIn(Duration.ofSeconds(duration))
            .sign();
    }

    /**
     * Generate token with custom expiration
     */
    public String generateToken(String username, Set<String> roles, Long expirationSeconds) {
        return Jwt.issuer(issuer)
            .upn(username)
            .groups(roles)
            .expiresIn(Duration.ofSeconds(expirationSeconds))
            .sign();
    }
}
