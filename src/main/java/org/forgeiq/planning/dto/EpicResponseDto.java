package org.forgeiq.planning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.forgeiq.common.enums.ApprovalStatus;
import org.forgeiq.common.enums.IssueSource;
import org.forgeiq.common.enums.SyncStatus;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpicResponseDto {
    private Long id;
    private String breakownId;
    private String title;
    private String description;
    private Integer storyPoints;
    private Integer position;
    private IssueSource source;
    private String aceptanceCriteria;
    private SyncStatus syncStatus;
    private ApprovalStatus approvalStatus;
    private String issueKey;
    private String issueUrl;
    private LocalDateTime lastSyncedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
