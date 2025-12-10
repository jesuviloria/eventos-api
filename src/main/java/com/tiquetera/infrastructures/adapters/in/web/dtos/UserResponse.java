package com.tiquetera.infrastructures.adapters.in.web.dtos;

import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Boolean enabled;
    private Set<String> roles;
}
