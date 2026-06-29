package org.forgeiq.planning.dto;

import lombok.Builder;
import lombok.Data;
import org.forgeiq.common.enums.BreakdownStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class BreakdownListItemDto {
    private Long breakdownId;

    private String projectName;

    private String title;

    private String description;

    private String additionalContext;

    private BreakdownStatus breakdownStatus;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;
}
