package com.tiquetera.applications.usecases.event;

import com.tiquetera.domains.models.Event;
import com.tiquetera.domains.ports.in.FindEventUseCase;
import com.tiquetera.domains.ports.out.EventRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class FindEventUseCaseImpl implements FindEventUseCase {
    
    private final EventRepositoryPort eventRepository;
    
    @Inject
    public FindEventUseCaseImpl(EventRepositoryPort eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
    }
    
    @Override
    public List<Event> findAll(int page, int size, String sort) {
        return eventRepository.findAll(page, size, sort);
    }
    
    @Override
    public List<Event> findByCiudad(String ciudad, int page, int size) {
        return eventRepository.findByCiudad(ciudad, page, size);
    }
    
    @Override
    public List<Event> findByCategoria(String categoria, int page, int size) {
        return eventRepository.findByCategoria(categoria, page, size);
    }
    
    @Override
    public List<Event> findByVenueId(Long venueId, int page, int size) {
        return eventRepository.findByVenueId(venueId, page, size);
    }
    
    @Override
    public List<Event> findUpcomingEvents() {
        return eventRepository.findUpcomingEvents();
    }
    
    @Override
    public List<Event> findByCiudadAndCategoria(String ciudad, String categoria) {
        return eventRepository.findByCiudadAndCategoria(ciudad, categoria);
    }
    
    @Override
    public List<Event> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByDateRange(start, end);
    }
}
