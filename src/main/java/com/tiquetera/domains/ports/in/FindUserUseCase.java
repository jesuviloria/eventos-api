package com.tiquetera.domains.ports.in;

import com.tiquetera.domains.models.User;
import java.util.Optional;

public interface FindUserUseCase {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}
