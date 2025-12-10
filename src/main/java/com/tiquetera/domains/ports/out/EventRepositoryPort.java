package com.tiquetera.domains.ports.out;

import com.tiquetera.domains.models.Event;
import java.util.List;
import java.util.Optional;

/**
 * Repository port for Event - Interface that domain defines
 * Infrastructure will implement this
 */
public interface EventRepositoryPort {
    
    Event save(Event event);
    
    Optional<Event> findById(Long id);
    
    List<Event> findAll(int page, int size, String sort);
    
    List<Event> findByCiudad(String ciudad, int page, int size);
    
    List<Event> findByCategoria(String categoria, int page, int size);
    
    List<Event> findByVenueId(Long venueId, int page, int size);
    
    Optional<Event> findByNombre(String nombre);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
}
