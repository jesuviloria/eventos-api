package com.tiquetera.infrastructures.adapters.out.persistences.mappers;

import com.tiquetera.domains.models.Event;
import com.tiquetera.infrastructures.adapters.out.persistences.entities.EventEntity;
import com.tiquetera.infrastructures.adapters.out.persistences.entities.VenueEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface EventMapper {

    // De Entity a Domain
    @Mapping(target = "venueId", source = "venue.id")
    Event toDomain(EventEntity entity);

    // De Domain a Entity (sin venue)
    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EventEntity toEntity(Event domain);
}
