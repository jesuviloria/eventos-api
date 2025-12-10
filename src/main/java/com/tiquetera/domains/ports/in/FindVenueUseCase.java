package com.tiquetera.domains.ports.in;

import com.tiquetera.domains.models.Venue;
import java.util.List;

public interface FindVenueUseCase {
    Venue findById(Long id);
    List<Venue> findAll(int page, int size, String sort);
    List<Venue> findByCiudad(String ciudad, int page, int size);
    List<Venue> findByCapacidadMinima(Integer capacidadMinima, int page, int size);
}

