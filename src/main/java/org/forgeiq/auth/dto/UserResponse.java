package org.forgeiq.auth.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponse {
    private Long id;
    private String email;
    private String role;
}
