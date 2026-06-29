package org.forgeiq.planning.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubtaskDto {

    private String issueKey;

    private String title;

    private String description;

    private String acceptanceCriteria;
}