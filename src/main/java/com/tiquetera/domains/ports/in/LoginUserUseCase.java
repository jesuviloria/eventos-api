package com.tiquetera.domains.ports.in;

public interface LoginUserUseCase {
    String execute(String username, String password);
}
