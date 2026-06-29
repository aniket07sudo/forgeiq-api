package org.forgeiq.planning.controller;

import lombok.RequiredArgsConstructor;
import org.forgeiq.auth.security.UserPrincipal;
import org.forgeiq.jira.dto.PushToJiraResponse;
import org.forgeiq.jira.service.JiraService;
import org.forgeiq.planning.dto.*;
import org.forgeiq.planning.service.BreakdownDraftService;
import org.forgeiq.planning.service.PlanningService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/planning")
@RequiredArgsConstructor
public class PlanningController {

    private final PlanningService planningService;
    private final BreakdownDraftService breakdownDraftService;
    private final JiraService jiraService;

    @PostMapping("/breakdown/{breakdownId}/push")
    public ResponseEntity<PushToJiraResponse> pushToJira(@PathVariable Long breakdownId, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        PushToJiraResponse response = jiraService.pushBreakdown(breakdownId, principal.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/breakdown")
    public ResponseEntity<Long> breakdown(@RequestBody BreakdownRequestDto request, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
        Long result = planningService.breakdownRequirement(request, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/breakdown")
    public ResponseEntity<BreakdownDashboardResponseDto> getBreakdowns(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
        BreakdownDashboardResponseDto result = planningService.getBreakdowns(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/breakdown/{breakdownId}")
    public ResponseEntity<BreakdownDetailResponseDto> getBreakdownDetails(
            @PathVariable Long breakdownId,
            Authentication authentication
    ) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        BreakdownDetailResponseDto result =
                planningService.getBreakdownDetails(breakdownId, userId);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/breakdown/draft")
    public ResponseEntity<Long> saveDraft(@RequestBody BreakdownRequestDto request) {
        Long result = breakdownDraftService.saveDraft(request);
        return ResponseEntity.ok(result);
    }
}
