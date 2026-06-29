package org.forgeiq.jira.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JiraIssueType {

    EPIC("Epic"),
    STORY("Story"),
    SUBTASK("Subtask"),
    TASK("Task"),
    BUG("Bug"),
    SPIKE("Spike");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}