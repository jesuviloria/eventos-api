package com.tiquetera.infrastructures.adapters.out.persistences.repositories;

import com.tiquetera.infrastructures.adapters.out.persistences.entities.RoleEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class RoleJpaRepository implements PanacheRepository<RoleEntity> {
    
    public Optional<RoleEntity> findByName(String name) {
        return find("name", name).firstResultOptional();
    }
}
