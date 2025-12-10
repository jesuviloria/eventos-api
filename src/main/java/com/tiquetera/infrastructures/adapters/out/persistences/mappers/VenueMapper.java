package com.tiquetera.infrastructures.adapters.out.persistences.mappers;

import com.tiquetera.domains.models.Venue;
import com.tiquetera.infrastructures.adapters.out.persistences.entities.VenueEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface VenueMapper {

    Venue toDomain(VenueEntity entity);

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VenueEntity toEntity(Venue domain);
}
