package org.forgeiq.planning.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoryDto {

    private String id;
    private String issueKey;
    private String title;
    private String description;
    private Integer storyPoints;

    private List<String> acceptanceCriteria;
    private List<SubtaskDto> subtasks;
}