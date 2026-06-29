package org.forgeiq.project.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JiraConnectionSummaryDto {

    private Long id;

    private String name;

    private String baseUrl;

    private String jiraProjectKey;

    private String jiraProjectName;
}