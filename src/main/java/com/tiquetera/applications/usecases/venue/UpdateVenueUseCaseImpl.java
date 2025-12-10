package com.tiquetera.applications.usecases.venue;

import com.tiquetera.domains.models.Venue;
import com.tiquetera.domains.ports.in.UpdateVenueUseCase;
import com.tiquetera.domains.ports.out.VenueRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateVenueUseCaseImpl implements UpdateVenueUseCase {

    private final VenueRepositoryPort venueRepository;

    @Inject
    public UpdateVenueUseCaseImpl(VenueRepositoryPort venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public Venue execute(Long id, Venue venue) {
        if (!venueRepository.existsById(id)) {
            throw new NotFoundException("Venue not found with id: " + id);
        }

        venue.validate();

        venueRepository.findByNombre(venue.getNombre())
            .ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalArgumentException("Another venue with this name already exists");
                }
            });

        venue.setId(id);
        return venueRepository.save(venue);
    }
}
