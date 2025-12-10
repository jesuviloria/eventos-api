package com.tiquetera.services;

import com.tiquetera.dtos.EventDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class EventService {
    
    private final Map<Long, EventDTO> events = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @Inject
    VenueService venueService;
    
    public EventDTO create(EventDTO event) {
        // Validar que el venue existe
        if (!venueService.existsById(event.getVenueId())) {
            throw new IllegalArgumentException("El venue especificado no existe");
        }
        
        // Validar nombre único
        if (events.values().stream()
                .anyMatch(e -> e.getNombre().equalsIgnoreCase(event.getNombre()))) {
            throw new IllegalArgumentException("Ya existe un evento con ese nombre");
        }
        
        // Validar que fechaInicio < fechaFin
        if (event.getFechaInicio().isAfter(event.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
        }
        
        event.setId(idGenerator.getAndIncrement());
        
        // Agregar nombre del venue para la respuesta
        venueService.findById(event.getVenueId())
            .ifPresent(venue -> event.setVenueNombre(venue.getNombre()));
        
        events.put(event.getId(), event);
        return event;
    }
    
    public List<EventDTO> findAll() {
        return new ArrayList<>(events.values());
    }
    
    public Optional<EventDTO> findById(Long id) {
        return Optional.ofNullable(events.get(id));
    }
    
    public List<EventDTO> findByVenueId(Long venueId) {
        return events.values().stream()
                .filter(e -> e.getVenueId().equals(venueId))
                .toList();
    }
    
    public List<EventDTO> findByCiudad(String ciudad) {
        return events.values().stream()
                .filter(e -> e.getCiudad().equalsIgnoreCase(ciudad))
                .toList();
    }
    
    public List<EventDTO> findByCategoria(String categoria) {
        return events.values().stream()
                .filter(e -> e.getCategoria() != null && 
                            e.getCategoria().equalsIgnoreCase(categoria))
                .toList();
    }
    
    public EventDTO update(Long id, EventDTO event) {
        if (!events.containsKey(id)) {
            throw new IllegalArgumentException("Evento no encontrado");
        }
        
        // Validar que el venue existe
        if (!venueService.existsById(event.getVenueId())) {
            throw new IllegalArgumentException("El venue especificado no existe");
        }
        
        // Validar nombre único (excluyendo el mismo evento)
        if (events.values().stream()
                .filter(e -> !e.getId().equals(id))
                .anyMatch(e -> e.getNombre().equalsIgnoreCase(event.getNombre()))) {
            throw new IllegalArgumentException("Ya existe otro evento con ese nombre");
        }
        
        // Validar fechas
        if (event.getFechaInicio().isAfter(event.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
        }
        
        event.setId(id);
        
        // Agregar nombre del venue
        venueService.findById(event.getVenueId())
            .ifPresent(venue -> event.setVenueNombre(venue.getNombre()));
        
        events.put(id, event);
        return event;
    }
    
    public void delete(Long id) {
        if (!events.containsKey(id)) {
            throw new IllegalArgumentException("Evento no encontrado");
        }
        events.remove(id);
    }
}
