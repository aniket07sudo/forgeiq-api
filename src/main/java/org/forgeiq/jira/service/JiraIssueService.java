package org.forgeiq.jira.service;

import lombok.RequiredArgsConstructor;
import org.forgeiq.jira.client.JiraClient;
import org.forgeiq.jira.dto.CreateIssueRequest;
import org.forgeiq.jira.dto.CreateIssueResponse;
import org.forgeiq.jira.entity.JiraConnection;
import org.forgeiq.jira.enums.JiraIssueType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JiraIssueService {

    private static final String ISSUE_API = "/rest/api/3/issue";

    private final JiraClient jiraClient;

    private CreateIssueRequest.Description toDescription(String value) {

        return CreateIssueRequest.Description.builder()
                .type("doc")
                .version(1)
                .content(List.of(
                        CreateIssueRequest.Content.builder()
                                .type("paragraph")
                                .content(List.of(
                                        CreateIssueRequest.Text.builder()
                                                .type("text")
                                                .text(value == null ? "" : value)
                                                .build()
                                ))
                                .build()
                ))
                .build();
    }

    public CreateIssueResponse createIssue(JiraConnection connection, String projectKey, String summary, String description, JiraIssueType issueType, String parentKey, Map<String, Object> customFields) {

        CreateIssueRequest.Fields.FieldsBuilder fieldsBuilder = CreateIssueRequest.Fields.builder()
                .project(new CreateIssueRequest.Project(projectKey))
                .summary(summary)
                .description(toDescription(description))
                .issueType(
                        new CreateIssueRequest.IssueType(issueType.getValue())
                );

        if (parentKey != null) {
            fieldsBuilder.parent(
                    new CreateIssueRequest.Parent(parentKey)
            );
        }

        CreateIssueRequest.Fields fields = fieldsBuilder.build();

        if (customFields != null && !customFields.isEmpty()) {
            fields.getCustomFields().putAll(customFields);
        }

        CreateIssueRequest request = CreateIssueRequest.builder()
                .fields(fields)
                .build();

        CreateIssueResponse response = jiraClient.post(
                connection,
                ISSUE_API,
                request,
                CreateIssueResponse.class
        );

        return response;
    }
}
