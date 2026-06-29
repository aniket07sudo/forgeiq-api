package org.forgeiq.planning.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.forgeiq.common.enums.BreakdownStatus;
import org.forgeiq.common.enums.PriorityEnum;
import org.forgeiq.common.enums.StorySource;
import org.forgeiq.planning.dto.BreakdownRequestDto;
import org.forgeiq.planning.dto.BreakdownResponseDto;
import org.forgeiq.planning.dto.StoryDto;
import org.forgeiq.planning.dto.SubtaskDto;
import org.forgeiq.planning.entity.*;
import org.forgeiq.planning.repository.BreakdownRepository;
import org.forgeiq.planning.repository.EpicRepository;
import org.forgeiq.planning.repository.TaskStatusRepository;
import org.forgeiq.project.Repository.ProjectRepository;
import org.forgeiq.project.Repository.ProjectStatusRepository;
import org.forgeiq.project.entity.Project;
import org.forgeiq.project.entity.ProjectStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BreakdownPersistenceService {

    private final ProjectRepository projectRepository;
    private final EpicRepository epicRepository;
    private final BreakdownRepository breakdownRepository;
    private final ProjectStatusRepository projectStatusRepository;
    private final TaskStatusRepository taskStatusRepository;

    public Long saveBreakdown(BreakdownResponseDto response, BreakdownRequestDto requirement, Long userId) {
        Project project = projectRepository.findByIdAndJiraConnection_User_Id(requirement.getProjectId(), userId).orElseThrow(() -> new RuntimeException("Project not found"));

        ProjectStatus defaultProjectStatus =
                projectStatusRepository
                        .findFirstByProjectOrderByPositionAsc(project)
                        .orElseThrow(() -> new RuntimeException("Default story status not found"));

        TaskStatus defaultSubtaskStatus = taskStatusRepository.findFirstByProjectOrderByPositionAsc(project)
                .orElseThrow(() -> new RuntimeException("Default story status not found"));

        Breakdown breakdown = Breakdown.builder()
                .project(project)
                .title(requirement.getTitle())
                .description(requirement.getDescription())
                .additionalContext(requirement.getAdditionalContext())
                .status(BreakdownStatus.GENERATED)
                .build();

        Long breakdownId = breakdownRepository.save(breakdown).getId();

        Epic epic = new Epic();
        epic.setBreakdown(breakdown);
        epic.setTitle(response.getEpic().getTitle());
        epic.setStoryPoints(response.getEpic().getStoryPoints());
        epic.setDescription(response.getEpic().getDescription());
//        epic.setStorySource(StorySource.AI);
        List<Story> stories = new ArrayList<>();
        for (StoryDto storyDto : response.getStories()) {
            Story story = new Story();
            story.setIssueKey(storyDto.getIssueKey());
            story.setEpic(epic);
            story.setTitle(storyDto.getTitle());
            story.setDescription(storyDto.getDescription());
            story.setStoryPoints(storyDto.getStoryPoints());
            story.setPriority(PriorityEnum.MEDIUM);
            story.setPosition(0);
            story.setProjectStatus(defaultProjectStatus);
//            story.setAcceptanceCriteria(String.join("\n" ,storyDto.getAcceptanceCriteria()));
            story.setSource(StorySource.AI);

            List<Task> subtasks = new ArrayList<>();

            for (SubtaskDto task : storyDto.getSubtasks()) {
                Task subtask = new Task();
                subtask.setTitle(task.getTitle());
                subtask.setDescription(task.getDescription());
                subtask.setIssueKey(task.getIssueKey());
                subtask.setAcceptanceCriteria(task.getAcceptanceCriteria());
                subtask.setStory(story);
                subtask.setPosition(0);
                subtask.setTaskStatus(defaultSubtaskStatus);
                subtasks.add(subtask);
            }

            story.setSubtasks(subtasks);
            stories.add(story);
        }

        epic.setStories(stories);
        epicRepository.save(epic);
        return breakdownId;
    }
}
