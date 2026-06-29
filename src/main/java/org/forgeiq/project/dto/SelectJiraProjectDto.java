package org.forgeiq.project.dto;

import lombok.Data;

@Data
public class SelectJiraProjectDto {

    private String projectId;

    private String projectKey;

    private String projectName;
}
