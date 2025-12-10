package com.tiquetera.services;

import com.tiquetera.dtos.EventDTO;
import com.tiquetera.entities.EventEntity;
import com.tiquetera.entities.VenueEntity;
import com.tiquetera.repositories.EventRepository;
import com.tiquetera.repositories.VenueRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EventService {
    
    @Inject
    EventRepository eventRepository;
    
    @Inject
    VenueRepository venueRepository;
    
    @Transactional
    public EventDTO create(EventDTO dto) {
        // Validate unique name
        if (eventRepository.findByNombre(dto.getNombre()).isPresent()) {
            throw new IllegalArgumentException("An event with this name already exists");
        }
        
        // Validate venue exists
        VenueEntity venue = venueRepository.findByIdOptional(dto.getVenueId())
            .orElseThrow(() -> new NotFoundException("Venue not found"));
        
        // Validate dates
        if (dto.getFechaInicio().isAfter(dto.getFechaFin())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        
        EventEntity entity = EventEntity.builder()
            .nombre(dto.getNombre())
            .descripcion(dto.getDescripcion())
            .fechaInicio(dto.getFechaInicio())
            .fechaFin(dto.getFechaFin())
            .venue(venue)
            .ciudad(dto.getCiudad())
            .categoria(dto.getCategoria())
            .build();
        
        eventRepository.persist(entity);
        return toDTO(entity);
    }
    
    public List<EventDTO> findAll(int pageIndex, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy != null ? sortBy : "fechaInicio");
        Page page = Page.of(pageIndex, pageSize);
        
        return eventRepository.findAll(sort)
            .page(page)
            .list()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public EventDTO findById(Long id) {
        return eventRepository.findByIdOptional(id)
            .map(this::toDTO)
            .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
    }
    
    public List<EventDTO> findByCiudad(String ciudad, int pageIndex, int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return eventRepository.findByCiudad(ciudad, page, Sort.by("fechaInicio"))
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<EventDTO> findByCategoria(String categoria, int pageIndex, int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return eventRepository.findByCategoria(categoria, page, Sort.by("fechaInicio"))
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<EventDTO> findByVenueId(Long venueId, int pageIndex, int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return eventRepository.findByVenueId(venueId, page)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public EventDTO update(Long id, EventDTO dto) {
        EventEntity entity = eventRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
        
        // Validate unique name (excluding current event)
        eventRepository.findByNombre(dto.getNombre())
            .ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalArgumentException("Another event with this name already exists");
                }
            });
        
        // Validate venue exists
        VenueEntity venue = venueRepository.findByIdOptional(dto.getVenueId())
            .orElseThrow(() -> new NotFoundException("Venue not found"));
        
        // Validate dates
        if (dto.getFechaInicio().isAfter(dto.getFechaFin())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFechaInicio(dto.getFechaInicio());
        entity.setFechaFin(dto.getFechaFin());
        entity.setVenue(venue);
        entity.setCiudad(dto.getCiudad());
        entity.setCategoria(dto.getCategoria());
        
        return toDTO(entity);
    }
    
    @Transactional
    public void delete(Long id) {
        if (!eventRepository.deleteById(id)) {
            throw new NotFoundException("Event not found with id: " + id);
        }
    }
    
    private EventDTO toDTO(EventEntity entity) {
        return EventDTO.builder()
            .id(entity.getId())
            .nombre(entity.getNombre())
            .descripcion(entity.getDescripcion())
            .fechaInicio(entity.getFechaInicio())
            .fechaFin(entity.getFechaFin())
            .venueId(entity.getVenue().getId())
            .venueNombre(entity.getVenue().getNombre())
            .ciudad(entity.getCiudad())
            .categoria(entity.getCategoria())
            .build();
    }
}
