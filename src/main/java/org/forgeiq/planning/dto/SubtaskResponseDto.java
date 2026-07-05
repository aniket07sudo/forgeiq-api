package org.forgeiq.planning.dto;

import lombok.Builder;
import lombok.Getter;
import org.forgeiq.common.enums.ApprovalStatus;
import org.forgeiq.common.enums.IssueSource;
import org.forgeiq.common.enums.SyncStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class SubtaskResponseDto {

    private Long id;

    private Long storyId;

    private String title;

    private String description;

    private String acceptanceCriteria;

    private SyncStatus syncStatus;

    private ApprovalStatus approvalStatus;

    private Integer position;

    private Long statusId;

    private String statusName;

    private IssueSource source;

    private String issueKey;

    private String issueUrl;

    private LocalDateTime lastSyncedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}