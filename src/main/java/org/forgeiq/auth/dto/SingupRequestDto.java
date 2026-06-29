package org.forgeiq.auth.dto;

import lombok.Data;

@Data
public class SingupRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userID;
}
