package com.tiquetera.infrastructures.adapters.out.persistences.repositories;

import com.tiquetera.infrastructures.adapters.out.persistences.entities.VenueEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class VenueJpaRepository implements PanacheRepository<VenueEntity> {
    
    /**
     * Find venues by city ordered by capacity
     */
    public List<VenueEntity> findByCiudadOrderByCapacidad(String ciudad) {
        return find("#VenueEntity.findByCiudadOrderByCapacidad",
            Parameters.with("ciudad", ciudad))
            .list();
    }
    
    /**
     * Find large venues (capacity >= minCapacidad)
     */
    public List<VenueEntity> findLargeVenues(Integer minCapacidad) {
        return find("#VenueEntity.findLargeVenues",
            Parameters.with("minCapacidad", minCapacidad))
            .list();
    }
    
    /**
     * Find venues with events (JOIN FETCH)
     */
    public List<VenueEntity> findAllWithEvents() {
        return find("SELECT DISTINCT v FROM VenueEntity v LEFT JOIN FETCH v.events")
            .list();
    }
}
