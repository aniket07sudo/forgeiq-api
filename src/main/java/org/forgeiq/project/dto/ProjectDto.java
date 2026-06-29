package org.forgeiq.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.forgeiq.project.enums.ProjectSetupStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private int storiesCount;
    private int storiesPoints;
    private ProjectSetupStatus status;
    private JiraConnectionSummaryDto jiraConnection;
    private String jiraConnections;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}