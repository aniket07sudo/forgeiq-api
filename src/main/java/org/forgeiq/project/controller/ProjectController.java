package org.forgeiq.project.controller;


import lombok.RequiredArgsConstructor;
import org.forgeiq.auth.security.UserPrincipal;
import org.forgeiq.project.dto.*;
import org.forgeiq.project.service.ProjectJiraService;
import org.forgeiq.project.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectJiraService projectJiraService;

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getProjects(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        List<ProjectDto> result = projectService.getProjects(principal.getId());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Long> createProject(@RequestBody ProjectRequestDto request, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long result = projectService.saveProject(request, principal.getId());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{projectId}/jira/connect")
    public ResponseEntity<Void> connectJira(@PathVariable Long projectId, @RequestBody ConnectJiraRequestDto request, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        projectJiraService.connectJira(projectId, request, principal.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/jira/getProjects")
    public ResponseEntity<JiraProjectsResponseDto> getJiraProjects(@PathVariable Long projectId, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        JiraProjectsResponseDto result = projectJiraService.getJiraProjects(principal.getId(), projectId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{projectId}/jira/selectProject")
    public ResponseEntity<Void> selectJiraProject(@PathVariable Long projectId, @RequestBody SelectJiraProjectDto request, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        projectJiraService.selectJiraProject(principal.getId(), projectId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/jira/getProjectStatus")
    public ResponseEntity<ProjectStatusResponseDto> getProjectStatus(@PathVariable Long projectId, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        ProjectStatusResponseDto result = projectJiraService.getProjectStatus(principal.getId(), projectId);
        return ResponseEntity.ok(result);
    }
}
