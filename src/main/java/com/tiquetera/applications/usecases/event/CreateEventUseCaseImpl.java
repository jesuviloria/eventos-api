package com.tiquetera.applications.usecases.event;

import com.tiquetera.domains.models.Event;
import com.tiquetera.domains.ports.in.CreateEventUseCase;
import com.tiquetera.domains.ports.out.EventRepositoryPort;
import com.tiquetera.domains.ports.out.VenueRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateEventUseCaseImpl implements CreateEventUseCase {
    
    private final EventRepositoryPort eventRepository;
    private final VenueRepositoryPort venueRepository;
    
    @Inject
    public CreateEventUseCaseImpl(
            EventRepositoryPort eventRepository,
            VenueRepositoryPort venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }
    
    @Override
    public Event execute(Event event) {
        // Business validation
        event.validate();
        
        // Check unique name
        if (eventRepository.findByNombre(event.getNombre()).isPresent()) {
            throw new IllegalArgumentException("An event with this name already exists");
        }
        
        // Verify venue exists
        if (!venueRepository.existsById(event.getVenueId())) {
            throw new IllegalArgumentException("Venue not found");
        }
        
        // Save and return
        return eventRepository.save(event);
    }
}
