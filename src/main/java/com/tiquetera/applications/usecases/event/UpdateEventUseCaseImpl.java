package com.tiquetera.applications.usecases.event;

import com.tiquetera.domains.models.Event;
import com.tiquetera.domains.ports.in.UpdateEventUseCase;
import com.tiquetera.domains.ports.out.EventRepositoryPort;
import com.tiquetera.domains.ports.out.VenueRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateEventUseCaseImpl implements UpdateEventUseCase {
    
    private final EventRepositoryPort eventRepository;
    private final VenueRepositoryPort venueRepository;
    
    @Inject
    public UpdateEventUseCaseImpl(
            EventRepositoryPort eventRepository,
            VenueRepositoryPort venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }
    
    @Override
    public Event execute(Long id, Event event) {
        // Verify event exists
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException("Event not found with id: " + id);
        }
        
        // Business validation
        event.validate();
        
        // Check unique name (excluding current event)
        eventRepository.findByNombre(event.getNombre())
            .ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalArgumentException("Another event with this name already exists");
                }
            });
        
        // Verify venue exists
        if (!venueRepository.existsById(event.getVenueId())) {
            throw new IllegalArgumentException("Venue not found");
        }
        
        // Set id and save
        event.setId(id);
        return eventRepository.save(event);
    }
}