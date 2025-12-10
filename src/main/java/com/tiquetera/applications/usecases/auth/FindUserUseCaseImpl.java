package com.tiquetera.applications.usecases.auth;

import com.tiquetera.domains.models.User;
import com.tiquetera.domains.ports.in.FindUserUseCase;
import com.tiquetera.domains.ports.out.UserRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class FindUserUseCaseImpl implements FindUserUseCase {

    private final UserRepositoryPort userRepository;

    @Inject
    public FindUserUseCaseImpl(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
