package org.forgeiq.planning.dto;

import lombok.Builder;
import lombok.Getter;
import org.forgeiq.common.enums.ApprovalStatus;
import org.forgeiq.common.enums.IssueSource;
import org.forgeiq.common.enums.PriorityEnum;
import org.forgeiq.common.enums.SyncStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class StoryResponseDto {

    private Long id;

    private Long epicId;

    private String title;

    private String description;

    private Integer storyPoints;

    private PriorityEnum priority;

    private String acceptanceCriteria;

    private Integer position;

    private Long statusId;

    private String statusName; // Optional but useful for UI

    private IssueSource source;

    private SyncStatus syncStatus;

    private ApprovalStatus approvalStatus;

    private String issueKey;

    private String issueUrl;

    private LocalDateTime lastSyncedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
//
//    // Optional if you want to return technical tasks with each story
//    private List<TechnicalTaskResponseDto> technicalTasks;
}