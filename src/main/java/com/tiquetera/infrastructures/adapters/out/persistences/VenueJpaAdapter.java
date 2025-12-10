package com.tiquetera.infrastructures.adapters.out.persistences;

import com.tiquetera.domains.models.Venue;
import com.tiquetera.domains.ports.out.VenueRepositoryPort;
import com.tiquetera.infrastructures.adapters.out.persistences.entities.VenueEntity;
import com.tiquetera.infrastructures.adapters.out.persistences.mappers.VenueMapper;
import com.tiquetera.infrastructures.adapters.out.persistences.repositories.VenueJpaRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class VenueJpaAdapter implements VenueRepositoryPort {

    @Inject
    VenueJpaRepository venueJpaRepository;

    @Inject
    VenueMapper venueMapper;

    @Override
    @Transactional
    public Venue save(Venue venue) {
        VenueEntity entity = venueMapper.toEntity(venue);
        venueJpaRepository.persist(entity);
        return venueMapper.toDomain(entity);
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return venueJpaRepository.findByIdOptional(id)
            .map(venueMapper::toDomain);
    }

    @Override
    public List<Venue> findAll(int page, int size, String sort) {
        Sort s = Sort.by(sort != null ? sort : "nombre");
        return venueJpaRepository.findAll(s)
            .page(Page.of(page, size))
            .list()
            .stream()
            .map(venueMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Venue> findByCiudad(String ciudad, int page, int size) {
        return venueJpaRepository.find("ciudad", ciudad)
            .page(Page.of(page, size))
            .list()
            .stream()
            .map(venueMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Venue> findByCapacidadMinima(Integer capacidadMinima, int page, int size) {
        return venueJpaRepository.find("capacidad >= ?1", capacidadMinima)
            .page(Page.of(page, size))
            .list()
            .stream()
            .map(venueMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Venue> findByNombre(String nombre) {
        return venueJpaRepository.find("nombre", nombre)
            .firstResultOptional()
            .map(venueMapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        venueJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return venueJpaRepository.findByIdOptional(id).isPresent();
    }
}