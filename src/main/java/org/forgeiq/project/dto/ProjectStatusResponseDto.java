package org.forgeiq.project.dto;

import lombok.Builder;
import lombok.Data;
import org.forgeiq.project.enums.ProjectSetupStatus;

@Builder
@Data
public class ProjectStatusResponseDto {

    private String jiraSite;

    private String jiraProjectName;

    private String jiraProjectKey;

    private String jiraAccount;

    private ProjectSetupStatus status;
}
