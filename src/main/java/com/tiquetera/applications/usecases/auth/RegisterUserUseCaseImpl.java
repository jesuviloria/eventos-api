package com.tiquetera.applications.usecases.auth;

import com.tiquetera.domains.models.User;
import com.tiquetera.domains.ports.in.RegisterUserUseCase;
import com.tiquetera.domains.ports.out.UserRepositoryPort;
import com.tiquetera.infrastructures.configs.PasswordService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Set;

@ApplicationScoped
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordService passwordService;

    @Inject
    public RegisterUserUseCaseImpl(UserRepositoryPort userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    public User execute(User user) {
        // Validaciones de negocio
        user.validate();

        // Verificar unicidad
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Encriptar password
        String hashedPassword = passwordService.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        // Asignar rol USER por defecto
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of("USER"));
        }

        // Habilitar por defecto
        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }

        // Guardar
        return userRepository.save(user);
    }
}
