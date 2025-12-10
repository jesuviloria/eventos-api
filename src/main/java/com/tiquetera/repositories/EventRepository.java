package com.tiquetera.repositories;

import com.tiquetera.entities.EventEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventRepository implements PanacheRepository<EventEntity> {
    
    public Optional<EventEntity> findByNombre(String nombre) {
        return find("nombre", nombre).firstResultOptional();
    }
    
    public List<EventEntity> findByCiudad(String ciudad, Page page, Sort sort) {
        return find("ciudad", Sort.by("fechaInicio"), ciudad)
            .page(page)
            .list();
    }
    
    public List<EventEntity> findByCategoria(String categoria, Page page, Sort sort) {
        return find("categoria", sort, categoria)
            .page(page)
            .list();
    }
    
    public List<EventEntity> findByVenueId(Long venueId, Page page) {
        return find("venue.id", venueId)
            .page(page)
            .list();
    }
    
    public List<EventEntity> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return find("fechaInicio >= ?1 AND fechaFin <= ?2", start, end).list();
    }
    
    public long countByCiudad(String ciudad) {
        return count("ciudad", ciudad);
    }
}
