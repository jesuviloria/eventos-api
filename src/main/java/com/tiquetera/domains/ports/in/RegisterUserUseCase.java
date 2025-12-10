package com.tiquetera.domains.ports.in;

import com.tiquetera.domains.models.User;

public interface RegisterUserUseCase {
    User execute(User user);
}
