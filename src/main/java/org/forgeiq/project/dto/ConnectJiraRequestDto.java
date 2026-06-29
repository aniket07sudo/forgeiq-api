package org.forgeiq.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectJiraRequestDto {

    private String name;

    private String baseUrl;

    private String email;

    private String apiToken;
}
