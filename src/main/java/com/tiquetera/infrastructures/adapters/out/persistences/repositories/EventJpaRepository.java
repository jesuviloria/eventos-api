package com.tiquetera.infrastructures.adapters.out.persistences.repositories;

import com.tiquetera.infrastructures.adapters.out.persistences.entities.EventEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventJpaRepository implements PanacheRepository<EventEntity> {
}
