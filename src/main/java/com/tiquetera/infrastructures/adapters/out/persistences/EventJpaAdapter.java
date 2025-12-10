package com.tiquetera.infrastructures.adapters.out.persistences;

import com.tiquetera.domains.models.Event;
import com.tiquetera.domains.ports.out.EventRepositoryPort;
import com.tiquetera.infrastructures.adapters.out.persistences.entities.EventEntity;
import com.tiquetera.infrastructures.adapters.out.persistences.entities.VenueEntity;
import com.tiquetera.infrastructures.adapters.out.persistences.mappers.EventMapper;
import com.tiquetera.infrastructures.adapters.out.persistences.repositories.EventJpaRepository;
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
public class EventJpaAdapter implements EventRepositoryPort {

    @Inject
    EventJpaRepository eventJpaRepository;

    @Inject
    VenueJpaRepository venueJpaRepository;

    @Inject
    EventMapper eventMapper;

    @Override
    @Transactional
    public Event save(Event event) {
        // Obtener el venue
        VenueEntity venueEntity = venueJpaRepository.findByIdOptional(event.getVenueId())
            .orElseThrow(() -> new IllegalArgumentException("Venue not found"));
        
        // Convertir domain a entity
        EventEntity entity;
        
        if (event.getId() != null) {
            // Update: buscar entidad existente
            entity = eventJpaRepository.findByIdOptional(event.getId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
            
            // Actualizar campos
            entity.setNombre(event.getNombre());
            entity.setDescripcion(event.getDescripcion());
            entity.setFechaInicio(event.getFechaInicio());
            entity.setFechaFin(event.getFechaFin());
            entity.setCiudad(event.getCiudad());
            entity.setCategoria(event.getCategoria());
            entity.setVenue(venueEntity);
        } else {
            // Create: nueva entidad
            entity = eventMapper.toEntity(event);
            entity.setVenue(venueEntity);
            eventJpaRepository.persist(entity);
        }
        
        return eventMapper.toDomain(entity);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventJpaRepository.findByIdOptional(id)
            .map(eventMapper::toDomain);
    }

    @Override
    public List<Event> findAll(int page, int size, String sort) {
        Sort s = Sort.by(sort != null ? sort : "fechaInicio");
        return eventJpaRepository.findAll(s)
            .page(Page.of(page, size))
            .list()
            .stream()
            .map(eventMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByCiudad(String ciudad, int page, int size) {
        return eventJpaRepository.find("ciudad", ciudad)
            .page(Page.of(page, size))
            .list()
            .stream()
            .map(eventMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByCategoria(String categoria, int page, int size) {
        return eventJpaRepository.find("categoria", categoria)
            .page(Page.of(page, size))
            .list()
            .stream()
            .map(eventMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByVenueId(Long venueId, int page, int size) {
        return eventJpaRepository.find("venue.id", venueId)
            .page(Page.of(page, size))
            .list()
            .stream()
            .map(eventMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Event> findByNombre(String nombre) {
        return eventJpaRepository.find("nombre", nombre)
            .firstResultOptional()
            .map(eventMapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        eventJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return eventJpaRepository.findByIdOptional(id).isPresent();
    }
}