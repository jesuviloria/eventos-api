package com.tiquetera.services;

import com.tiquetera.dtos.VenueDTO;
import com.tiquetera.entities.VenueEntity;
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
public class VenueService {
    
    @Inject
    VenueRepository venueRepository;
    
    @Transactional
    public VenueDTO create(VenueDTO dto) {
        // Validate unique name
        if (venueRepository.findByNombre(dto.getNombre()).isPresent()) {
            throw new IllegalArgumentException("A venue with this name already exists");
        }
        
        VenueEntity entity = VenueEntity.builder()
            .nombre(dto.getNombre())
            .ciudad(dto.getCiudad())
            .capacidad(dto.getCapacidad())
            .direccion(dto.getDireccion())
            .build();
        
        venueRepository.persist(entity);
        return toDTO(entity);
    }
    
    public List<VenueDTO> findAll(int pageIndex, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy != null ? sortBy : "nombre");
        Page page = Page.of(pageIndex, pageSize);
        
        return venueRepository.findAll(sort)
            .page(page)
            .list()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public VenueDTO findById(Long id) {
        return venueRepository.findByIdOptional(id)
            .map(this::toDTO)
            .orElseThrow(() -> new NotFoundException("Venue not found with id: " + id));
    }
    
    public List<VenueDTO> findByCiudad(String ciudad, int pageIndex, int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return venueRepository.findByCiudad(ciudad, page)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<VenueDTO> findByCapacidadMinima(Integer capacidadMinima, int pageIndex, int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return venueRepository.findByCapacidadMinima(capacidadMinima, page)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public VenueDTO update(Long id, VenueDTO dto) {
        VenueEntity entity = venueRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Venue not found with id: " + id));
        
        // Validate unique name (excluding current venue)
        venueRepository.findByNombre(dto.getNombre())
            .ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalArgumentException("Another venue with this name already exists");
                }
            });
        
        entity.setNombre(dto.getNombre());
        entity.setCiudad(dto.getCiudad());
        entity.setCapacidad(dto.getCapacidad());
        entity.setDireccion(dto.getDireccion());
        
        return toDTO(entity);
    }
    
    @Transactional
    public void delete(Long id) {
        if (!venueRepository.deleteById(id)) {
            throw new NotFoundException("Venue not found with id: " + id);
        }
    }
    
    public boolean existsById(Long id) {
        return venueRepository.findByIdOptional(id).isPresent();
    }
    
    private VenueDTO toDTO(VenueEntity entity) {
        return VenueDTO.builder()
            .id(entity.getId())
            .nombre(entity.getNombre())
            .ciudad(entity.getCiudad())
            .capacidad(entity.getCapacidad())
            .direccion(entity.getDireccion())
            .build();
    }
}
