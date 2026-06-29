package org.forgeiq.jira.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateIssueRequest {

    private Fields fields;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fields {

        private Project project;

        private String summary;

        private Description description;

        @JsonProperty("issuetype")
        private IssueType issueType;

        private Parent parent;

        @JsonIgnore
        @Builder.Default
        private Map<String, Object> customFields = new HashMap<>();

        @JsonAnyGetter
        public Map<String, Object> any() {
            return customFields;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Project {

        private String key;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IssueType {

        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Parent {

        private String key;
    }

    // =========================
    // Atlassian Document Format
    // =========================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Description {

        private String type;

        private Integer version;

        private List<Content> content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {

        private String type;

        private List<Text> content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Text {

        private String type;

        private String text;
    }
}