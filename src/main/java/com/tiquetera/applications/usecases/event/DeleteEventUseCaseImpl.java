package com.tiquetera.applications.usecases.event;

import com.tiquetera.domains.ports.in.DeleteEventUseCase;
import com.tiquetera.domains.ports.out.EventRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DeleteEventUseCaseImpl implements DeleteEventUseCase {
    
    private final EventRepositoryPort eventRepository;
    
    @Inject
    public DeleteEventUseCaseImpl(EventRepositoryPort eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    @Override
    public void execute(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }
}
