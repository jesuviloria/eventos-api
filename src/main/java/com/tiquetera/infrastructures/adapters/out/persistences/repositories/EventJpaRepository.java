package com.tiquetera.infrastructures.adapters.out.persistences.repositories;

import com.tiquetera.infrastructures.adapters.out.persistences.entities.EventEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventJpaRepository implements PanacheRepository<EventEntity> {
    
    /**
     * Find upcoming events (future events ordered by date)
     */
    public List<EventEntity> findUpcomingEvents() {
        return find("#EventEntity.findUpcomingEvents").list();
    }
    
    /**
     * Find events by city and category using named query
     */
    public List<EventEntity> findByCiudadAndCategoria(String ciudad, String categoria) {
        return find("#EventEntity.findByCiudadAndCategoria",
            Parameters.with("ciudad", ciudad).and("categoria", categoria))
            .list();
    }
    
    /**
     * Find event with venue (JOIN FETCH to avoid N+1)
     */
    public Optional<EventEntity> findByIdWithVenue(Long id) {
        return find("#EventEntity.findWithVenue", Parameters.with("id", id))
            .firstResultOptional();
    }
    
    /**
     * Find events in date range
     */
    public List<EventEntity> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return find("fechaInicio >= ?1 AND fechaFin <= ?2", start, end).list();
    }
    
    /**
     * Count events by venue
     */
    public Long countByVenueId(Long venueId) {
        return count("venue.id", venueId);
    }
}

