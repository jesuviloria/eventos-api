package com.tiquetera.applications.usecases.venue;

import com.tiquetera.domains.models.Venue;
import com.tiquetera.domains.ports.in.CreateVenueUseCase;
import com.tiquetera.domains.ports.out.VenueRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateVenueUseCaseImpl implements CreateVenueUseCase {

    private final VenueRepositoryPort venueRepository;

    @Inject
    public CreateVenueUseCaseImpl(VenueRepositoryPort venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public Venue execute(Venue venue) {
        venue.validate();

        venueRepository.findByNombre(venue.getNombre())
            .ifPresent(existing -> {
                throw new IllegalArgumentException("A venue with this name already exists");
            });

        return venueRepository.save(venue);
    }
}
