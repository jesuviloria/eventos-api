package com.tiquetera.infrastructures.adapters.out.persistences.repositories;

import com.tiquetera.infrastructures.adapters.out.persistences.entities.VenueEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VenueJpaRepository implements PanacheRepository<VenueEntity> {
}
