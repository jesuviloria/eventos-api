package com.tiquetera.infrastructures.adapters.out.persistences.mappers;

import com.tiquetera.domains.models.User;
import com.tiquetera.infrastructures.adapters.out.persistences.entities.UserEntity;
import com.tiquetera.infrastructures.adapters.out.persistences.entities.RoleEntity;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
    User toDomain(UserEntity entity);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity toEntity(User domain);

    @Named("rolesToStrings")
    default Set<String> rolesToStrings(Set<RoleEntity> roles) {
        return roles.stream()
            .map(RoleEntity::getName)
            .collect(Collectors.toSet());
    }
}
