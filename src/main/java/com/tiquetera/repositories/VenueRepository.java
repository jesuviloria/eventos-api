package com.tiquetera.repositories;

import com.tiquetera.entities.VenueEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VenueRepository implements PanacheRepository<VenueEntity> {
    
    public Optional<VenueEntity> findByNombre(String nombre) {
        return find("nombre", nombre).firstResultOptional();
    }
    
    public List<VenueEntity> findByCiudad(String ciudad, Page page) {
        return find("ciudad", Sort.ascending("nombre"), ciudad)
            .page(page)
            .list();
    }
    
    public List<VenueEntity> findByCapacidadMinima(Integer capacidadMinima, Page page) {
        return find("capacidad >= ?1", Sort.descending("capacidad"), capacidadMinima)
            .page(page)
            .list();
    }
    
    public long countByCiudad(String ciudad) {
        return count("ciudad", ciudad);
    }
}
