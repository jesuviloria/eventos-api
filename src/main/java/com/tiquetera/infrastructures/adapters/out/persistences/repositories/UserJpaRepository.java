package com.tiquetera.infrastructures.adapters.out.persistences.repositories;

import com.tiquetera.infrastructures.adapters.out.persistences.entities.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class UserJpaRepository implements PanacheRepository<UserEntity> {
    
    public Optional<UserEntity> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }
    
    public Optional<UserEntity> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
    
    public boolean existsByUsername(String username) {
        return count("username", username) > 0;
    }
    
    public boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }
}
