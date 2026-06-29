package org.forgeiq.jira.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PushToJiraResponse {

    private int epicsCreated;
    private int storiesCreated;
    private int subtasksCreated;

    private int epicsSkipped;
    private int storiesSkipped;
    private int subtasksSkipped;
}