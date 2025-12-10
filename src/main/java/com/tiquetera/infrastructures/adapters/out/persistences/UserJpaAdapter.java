package com.tiquetera.infrastructures.adapters.out.persistences;

import com.tiquetera.domains.models.User;
import com.tiquetera.domains.ports.out.UserRepositoryPort;
import com.tiquetera.infrastructures.adapters.out.persistences.entities.UserEntity;
import com.tiquetera.infrastructures.adapters.out.persistences.entities.RoleEntity;
import com.tiquetera.infrastructures.adapters.out.persistences.mappers.UserMapper;
import com.tiquetera.infrastructures.adapters.out.persistences.repositories.UserJpaRepository;
import com.tiquetera.infrastructures.adapters.out.persistences.repositories.RoleJpaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserJpaAdapter implements UserRepositoryPort {

    @Inject
    UserJpaRepository userJpaRepository;

    @Inject
    RoleJpaRepository roleJpaRepository;

    @Inject
    UserMapper userMapper;

    @Override
    @Transactional
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        
        // Asignar roles
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            Set<RoleEntity> roleEntities = user.getRoles().stream()
                .map(roleName -> roleJpaRepository.findByName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
            
            roleEntities.forEach(entity::addRole);
        }
        
        userJpaRepository.persist(entity);
        return userMapper.toDomain(entity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findByIdOptional(id)
            .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
            .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
            .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
