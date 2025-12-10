package com.tiquetera.services;

import com.tiquetera.dtos.VenueDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class VenueService {
    
    private final Map<Long, VenueDTO> venues = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public VenueDTO create(VenueDTO venue) {
        // Validar nombre único
        if (venues.values().stream()
                .anyMatch(v -> v.getNombre().equalsIgnoreCase(venue.getNombre()))) {
            throw new IllegalArgumentException("Ya existe un venue con ese nombre");
        }
        
        venue.setId(idGenerator.getAndIncrement());
        venues.put(venue.getId(), venue);
        return venue;
    }
    
    public List<VenueDTO> findAll() {
        return new ArrayList<>(venues.values());
    }
    
    public Optional<VenueDTO> findById(Long id) {
        return Optional.ofNullable(venues.get(id));
    }
    
    public List<VenueDTO> findByCiudad(String ciudad) {
        return venues.values().stream()
                .filter(v -> v.getCiudad().equalsIgnoreCase(ciudad))
                .toList();
    }
    
    public List<VenueDTO> findByCapacidadMinima(Integer capacidadMinima) {
        return venues.values().stream()
                .filter(v -> v.getCapacidad() >= capacidadMinima)
                .toList();
    }
    
    public VenueDTO update(Long id, VenueDTO venue) {
        if (!venues.containsKey(id)) {
            throw new IllegalArgumentException("Venue no encontrado");
        }
        
        // Validar nombre único (excluyendo el mismo venue)
        if (venues.values().stream()
                .filter(v -> !v.getId().equals(id))
                .anyMatch(v -> v.getNombre().equalsIgnoreCase(venue.getNombre()))) {
            throw new IllegalArgumentException("Ya existe otro venue con ese nombre");
        }
        
        venue.setId(id);
        venues.put(id, venue);
        return venue;
    }
    
    public void delete(Long id) {
        if (!venues.containsKey(id)) {
            throw new IllegalArgumentException("Venue no encontrado");
        }
        venues.remove(id);
    }
    
    public boolean existsById(Long id) {
        return venues.containsKey(id);
    }
}
