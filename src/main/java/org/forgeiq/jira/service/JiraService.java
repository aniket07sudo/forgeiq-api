package org.forgeiq.jira.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.forgeiq.common.enums.BreakdownStatus;
import org.forgeiq.common.enums.SyncStatus;
import org.forgeiq.jira.dto.CreateIssueResponse;
import org.forgeiq.jira.dto.PushToJiraResponse;
import org.forgeiq.jira.entity.JiraConnection;
import org.forgeiq.jira.enums.JiraIssueType;
import org.forgeiq.jira.repository.JiraConnectionRepository;
import org.forgeiq.planning.entity.Breakdown;
import org.forgeiq.planning.entity.Epic;
import org.forgeiq.planning.entity.Story;
import org.forgeiq.planning.entity.Task;
import org.forgeiq.planning.repository.BreakdownRepository;
import org.forgeiq.planning.repository.EpicRepository;
import org.forgeiq.planning.repository.StoryRepository;
import org.forgeiq.planning.repository.TaskRepository;
import org.forgeiq.project.entity.Project;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JiraService {

    private final BreakdownRepository breakdownRepository;
    private final EpicRepository epicRepository;
    private final StoryRepository storyRepository;
    private final TaskRepository taskRepository;
    private final JiraIssueService jiraIssueService;

    @Transactional
    public PushToJiraResponse pushBreakdown(Long breakdownId, Long userId) {
        Breakdown breakdown = breakdownRepository.findByIdAndProject_User_Id(breakdownId, userId).orElseThrow(() -> new RuntimeException("Breakdown not found"));
        JiraConnection connection = breakdown.getProject().getJiraConnection();

        if (connection.getApiToken() == null) {
            throw new RuntimeException("No Jira connection configured for project");
        }

        String projectKey = breakdown.getProject().getJiraProjectKey();

        int epicsCreated = 0;
        int storiesCreated = 0;
        int subtasksCreated = 0;

        int epicsSkipped = 0;
        int storiesSkipped = 0;
        int subtasksSkipped = 0;

        for (Epic epic : breakdown.getEpics()) {

            if (epic.getIssueKey() != null) {
                epicsSkipped++;
                continue;
            }

            CreateIssueResponse epicJiraResponse = jiraIssueService.createIssue(
                    connection,
                    projectKey,
                    epic.getTitle(),
                    epic.getDescription(),
                    JiraIssueType.EPIC,
                    null,
                    null
            );

            epicsCreated++;
            epic.setIssueKey(epicJiraResponse.getKey());
            epic.setSyncStatus(SyncStatus.SYNCED);
            epic.setLastSyncedAt(LocalDateTime.now());

            epicRepository.saveAndFlush(epic);

            for (Story story : epic.getStories()) {
                if (story.getIssueKey() != null) {
                    storiesSkipped++;
                    continue;
                }

                CreateIssueResponse storyJiraResponse = jiraIssueService.createIssue(
                        connection,
                        projectKey,
                        story.getTitle(),
                        story.getDescription(),
                        JiraIssueType.STORY,
                        epicJiraResponse.getKey(),
                        null
                );

                story.setIssueKey(storyJiraResponse.getKey());
                story.setSyncStatus(SyncStatus.SYNCED);
                story.setLastSyncedAt(LocalDateTime.now());
                storiesCreated++;

                storyRepository.saveAndFlush(story);

                for (Task task : story.getSubtasks()) {
                    if (task.getIssueKey() != null) {
                        subtasksSkipped++;
                        continue;
                    }
                    CreateIssueResponse taskJiraResponse = jiraIssueService.createIssue(
                            connection,
                            projectKey,
                            task.getTitle(),
                            task.getDescription(),
                            JiraIssueType.SUBTASK,
                            storyJiraResponse.getKey(),
                            null
                    );

                    subtasksCreated++;
                    task.setSyncStatus(SyncStatus.SYNCED);
                    task.setIssueKey(taskJiraResponse.getKey());
                    task.setLastSyncedAt(LocalDateTime.now());
                    taskRepository.saveAndFlush(task);
                }
            }
        }

        boolean synced = breakdown.getEpics().stream()
                .allMatch(epic -> epic.getIssueKey() != null &&
                        epic.getStories().stream()
                                .allMatch(story -> story.getIssueKey() != null &&
                                        story.getSubtasks().stream()
                                                .allMatch(task -> task.getIssueKey() != null)
                                ));
        breakdown.setStatus(synced ? BreakdownStatus.SYNCED : BreakdownStatus.OUT_OF_SYNC);
        breakdownRepository.saveAndFlush(breakdown);
        return PushToJiraResponse.builder()
                .storiesCreated(storiesCreated)
                .epicsCreated(epicsCreated)
                .subtasksCreated(subtasksCreated)
                .epicsSkipped(epicsSkipped)
                .storiesSkipped(storiesSkipped)
                .subtasksSkipped(subtasksSkipped)
                .build();
    }

    private String buildBreakdownUrl(Breakdown breakdown) {
        Project project = breakdown.getProject();

        if (project == null || project.getJiraConnection() == null) {
            return null;
        }

        String issueKey = breakdown.getEpics().stream()
                .map(Epic::getIssueKey)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        if (issueKey == null) {
            return null;
        }

        return project.getJiraConnection().getBaseUrl() + "/browse/" + issueKey;
    }
}
