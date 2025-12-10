package com.tiquetera.applications.usecases.venue;

import com.tiquetera.domains.ports.in.DeleteVenueUseCase;
import com.tiquetera.domains.ports.out.VenueRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DeleteVenueUseCaseImpl implements DeleteVenueUseCase {

    private final VenueRepositoryPort venueRepository;

    @Inject
    public DeleteVenueUseCaseImpl(VenueRepositoryPort venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public void execute(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new NotFoundException("Venue not found with id: " + id);
        }
        venueRepository.deleteById(id);
    }
}
