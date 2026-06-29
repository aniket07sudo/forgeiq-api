package org.forgeiq.project.service;
import lombok.RequiredArgsConstructor;
import org.forgeiq.jira.client.JiraClient;
import org.forgeiq.jira.entity.JiraConnection;
import org.forgeiq.jira.repository.JiraConnectionRepository;
import org.forgeiq.project.Repository.ProjectRepository;
import org.forgeiq.project.dto.ConnectJiraRequestDto;
import org.forgeiq.project.dto.JiraProjectsResponseDto;
import org.forgeiq.project.dto.ProjectStatusResponseDto;
import org.forgeiq.project.dto.SelectJiraProjectDto;
import org.forgeiq.project.entity.Project;
import org.forgeiq.project.enums.ProjectSetupStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectJiraService {

    private final ProjectRepository projectRepository;
    private final JiraConnectionRepository jiraConnectionRepository;
    private final JiraClient jiraClient;
    private static final String JIRA_PROJECTS_ENDPOINT =
            "/rest/api/3/project/search?orderBy=-lastIssueUpdatedTime";

    public void connectJira(Long projectId, ConnectJiraRequestDto request, Long userId) {
        Project project = projectRepository.findByIdAndUser_Id(projectId, userId).orElseThrow(() -> new RuntimeException("Project not found"));
        JiraConnection jiraConnection = JiraConnection.builder()
                .user(project.getUser())
                .name(request.getName())
                .baseUrl(request.getBaseUrl())
                .email(request.getEmail())
                .apiToken(request.getApiToken())
                .build();

        jiraConnectionRepository.save(jiraConnection);
        project.setJiraConnection(jiraConnection);
        projectRepository.save(project);
    }

    public JiraProjectsResponseDto getJiraProjects(Long userId, Long projectId) {
        Project project = projectRepository.findByIdAndUser_Id(projectId, userId).orElseThrow(() -> new RuntimeException("Project not found"));

        JiraConnection jiraConnection = project.getJiraConnection();

        if (jiraConnection == null) {
            throw new RuntimeException("Jira Connection not found");
        }

        return jiraClient.get(jiraConnection, JIRA_PROJECTS_ENDPOINT, JiraProjectsResponseDto.class);
    }

    public void selectJiraProject(Long userId, Long projectId, SelectJiraProjectDto request) {
        Project project = projectRepository.findByIdAndUser_Id(projectId, userId).orElseThrow(() -> new RuntimeException("Project not found"));

        if (project.getJiraConnection() == null) {
            throw new RuntimeException("Jira is not connected.");
        }

        project.setJiraProjectKey(request.getProjectKey());
        project.setJiraProjectId(request.getProjectId());
        project.setJiraProjectName(request.getProjectName());
        projectRepository.save(project);
    }

    public ProjectStatusResponseDto getProjectStatus(Long userId, Long projectId) {
        Project project = projectRepository.findByIdAndUser_Id(projectId, userId).orElseThrow(() -> new RuntimeException("Project not found"));

        if (project.getJiraConnection() == null) {
            throw new RuntimeException("Jira is not connected.");
        }
        JiraConnection connection = project.getJiraConnection();

        return ProjectStatusResponseDto.builder()
                .jiraProjectName(project.getJiraProjectName())
                .jiraProjectKey(project.getJiraProjectKey())
                .jiraAccount(connection.getEmail())
                .jiraSite(connection.getBaseUrl())
                .status(ProjectSetupStatus.READY)
                .build();
    }

}
