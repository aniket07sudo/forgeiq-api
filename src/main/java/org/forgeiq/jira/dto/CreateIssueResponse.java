package org.forgeiq.jira.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateIssueResponse {

    private String id;

    private String key;

    private String self;

}