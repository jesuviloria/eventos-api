package com.tiquetera.domains.ports.out;

import com.tiquetera.domains.models.Venue;
import java.util.List;
import java.util.Optional;

/**
 * Repository port for Venue - Interface that domain defines
 * Infrastructure will implement this
 */
public interface VenueRepositoryPort {
    
    Venue save(Venue venue);
    
    Optional<Venue> findById(Long id);
    
    List<Venue> findAll(int page, int size, String sort);
    
    List<Venue> findByCiudad(String ciudad, int page, int size);
    
    List<Venue> findByCapacidadMinima(Integer capacidadMinima, int page, int size);
    
    Optional<Venue> findByNombre(String nombre);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
}
