package com.tiquetera.infrastructures.configs;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordService {

    /**
     * Hash password using BCrypt
     */
    public String hashPassword(String plainPassword) {
        return BcryptUtil.bcryptHash(plainPassword);
    }

    /**
     * Verify password against hash
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BcryptUtil.matches(plainPassword, hashedPassword);
    }
}
