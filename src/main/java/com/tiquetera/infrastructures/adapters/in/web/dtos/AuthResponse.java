package com.tiquetera.infrastructures.adapters.in.web.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String type;
    private String username;
    private String email;
}
