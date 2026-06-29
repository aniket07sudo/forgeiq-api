package org.forgeiq.planning.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.forgeiq.common.enums.StorySource;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EpicDto {
    private Long Id;
    private String jiraIssueKey;
    private String title;
    private String description;
    private Integer storyPoints;
    private List<String> acceptanceCriteria;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}