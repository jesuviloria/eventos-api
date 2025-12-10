package com.tiquetera.applications.usecases.venue;

import com.tiquetera.domains.models.Venue;
import com.tiquetera.domains.ports.in.FindVenueUseCase;
import com.tiquetera.domains.ports.out.VenueRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class FindVenueUseCaseImpl implements FindVenueUseCase {

    private final VenueRepositoryPort venueRepository;

    @Inject
    public FindVenueUseCaseImpl(VenueRepositoryPort venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public Venue findById(Long id) {
        return venueRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Venue not found with id: " + id));
    }

    @Override
    public List<Venue> findAll(int page, int size, String sort) {
        return venueRepository.findAll(page, size, sort);
    }

    @Override
    public List<Venue> findByCiudad(String ciudad, int page, int size) {
        return venueRepository.findByCiudad(ciudad, page, size);
    }

    @Override
    public List<Venue> findByCapacidadMinima(Integer capacidadMinima, int page, int size) {
        return venueRepository.findByCapacidadMinima(capacidadMinima, page, size);
    }
}
