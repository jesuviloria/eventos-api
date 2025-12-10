package com.tiquetera.domains.ports.in;

import com.tiquetera.domains.models.Event;
import java.util.List;

public interface FindEventUseCase {
    Event findById(Long id);
    List<Event> findAll(int page, int size, String sort);
    List<Event> findByCiudad(String ciudad, int page, int size);
    List<Event> findByCategoria(String categoria, int page, int size);
    List<Event> findByVenueId(Long venueId, int page, int size);
}