package com.tiquetera.applications.usecases.auth;

import com.tiquetera.domains.models.User;
import com.tiquetera.domains.ports.in.LoginUserUseCase;
import com.tiquetera.domains.ports.out.UserRepositoryPort;
import com.tiquetera.infrastructures.configs.JwtService;
import com.tiquetera.infrastructures.configs.PasswordService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;

@ApplicationScoped
public class LoginUserUseCaseImpl implements LoginUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordService passwordService;
    private final JwtService jwtService;

    @Inject
    public LoginUserUseCaseImpl(
            UserRepositoryPort userRepository,
            PasswordService passwordService,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.jwtService = jwtService;
    }

    @Override
    public String execute(String username, String password) {
        // Buscar usuario
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new NotAuthorizedException("Invalid credentials"));

        // Verificar que esté habilitado
        if (!user.getEnabled()) {
            throw new NotAuthorizedException("User account is disabled");
        }

        // Verificar password
        if (!passwordService.verifyPassword(password, user.getPassword())) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        // Generar JWT
        return jwtService.generateToken(user.getUsername(), user.getRoles());
    }
}
