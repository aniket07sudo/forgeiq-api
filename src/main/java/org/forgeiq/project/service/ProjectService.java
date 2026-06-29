package org.forgeiq.project.service;

import lombok.RequiredArgsConstructor;
import org.forgeiq.auth.entity.User;
import org.forgeiq.auth.repository.UserRepository;
import org.forgeiq.planning.entity.TaskStatus;
import org.forgeiq.planning.repository.TaskStatusRepository;
import org.forgeiq.project.Repository.ProjectRepository;
import org.forgeiq.project.Repository.ProjectStatusRepository;
import org.forgeiq.project.dto.JiraConnectionSummaryDto;
import org.forgeiq.project.dto.ProjectDto;
import org.forgeiq.project.dto.ProjectRequestDto;
import org.forgeiq.project.entity.Project;
import org.forgeiq.project.entity.ProjectStatus;
import org.forgeiq.project.enums.ProjectSetupStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final ProjectStatusRepository projectStatusRepository;

    public List<ProjectDto> getProjects(Long userId) {
        return projectRepository.findAllByUser_Id(userId).stream().map(this::toDto).toList();
    }

    public Long saveProject(ProjectRequestDto request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Project project = Project.builder()
                .user(user)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        List<TaskStatus> statuses = List.of(
                create(project, "TODO", "To Do", 0, false),
                create(project, "IN_PROGRESS", "In Progress", 1, false),
                create(project, "BLOCKED", "Blocked", 2, false),
                create(project, "IN_REVIEW", "In Review", 3, false),
                create(project, "DONE", "Done", 4, true)
        );

        List<ProjectStatus> projectStatuses = List.of(
                createProject(project, "BACKLOG", "Backlog", 0, false),
                createProject(project, "PLANNED", "Planned", 1, false),
                createProject(project, "IN_PROGRESS", "In Progress", 2, false),
                createProject(project, "IN_REVIEW", "In Review", 3, false),
                createProject(project, "DONE", "Done", 4, true)
        );
        project = projectRepository.save(project);
        projectStatusRepository.saveAll(projectStatuses);
        taskStatusRepository.saveAll(statuses);

        return project.getId();
    }


    private ProjectStatus createProject(
            Project project,
            String key,
            String label,
            int position,
            boolean isFinal
    ) {
        return ProjectStatus.builder()
                .project(project)
                .statusKey(key)
                .label(label)
                .position(position)
                .isFinal(isFinal)
                .build();
    }

    private TaskStatus create(
            Project project,
            String key,
            String label,
            int position,
            boolean isFinal
    ) {
        return TaskStatus.builder()
                .project(project)
                .statusKey(key)
                .label(label)
                .position(position)
                .isFinal(isFinal)
                .build();
    }

    private ProjectDto toDto(Project project) {
        ProjectSetupStatus status;
        if (project.getJiraConnection() == null) {
            status = ProjectSetupStatus.CONNECT_JIRA;
        } else if (project.getJiraProjectKey() == null) {
            status = ProjectSetupStatus.SELECT_JIRA_PROJECT;
        } else {
            status = ProjectSetupStatus.READY;
        }

        JiraConnectionSummaryDto jiraConnection = null;

        if (project.getJiraConnection() != null) {
            jiraConnection = JiraConnectionSummaryDto.builder()
                    .id(project.getJiraConnection().getId())
                    .name(project.getJiraConnection().getName())
                    .baseUrl(project.getJiraConnection().getBaseUrl())
                    .jiraProjectKey(project.getJiraProjectKey())
                    .jiraProjectName(project.getName())
                    .build();
        }
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .jiraConnection(jiraConnection)
                .status(status)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
