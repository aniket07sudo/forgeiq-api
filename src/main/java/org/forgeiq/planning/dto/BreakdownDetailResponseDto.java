package org.forgeiq.planning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.forgeiq.common.enums.BreakdownStatus;
import org.forgeiq.project.dto.ProjectDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreakdownDetailResponseDto {

    private Long id;
    private String title;
    private String description;
    private String breakdownUrl;
    private String additionalContext;
    private BreakdownStatus status;
    private ProjectDto project;
    private Integer epicsCount;
    private Integer storiesCount;
    private Integer totalStoryPoints;
    private Integer technicalTasksCount;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
