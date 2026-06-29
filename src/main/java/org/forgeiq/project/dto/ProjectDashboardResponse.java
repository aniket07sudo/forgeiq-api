package org.forgeiq.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDashboardResponse {
    private ProjectOverviewDto summary;
    private ProjectDto table;
}
