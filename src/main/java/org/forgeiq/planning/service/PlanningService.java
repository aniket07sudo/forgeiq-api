package org.forgeiq.planning.service;

import lombok.RequiredArgsConstructor;
import org.forgeiq.auth.entity.User;
import org.forgeiq.auth.service.GeminiService;
import org.forgeiq.common.enums.BreakdownStatus;
import org.forgeiq.jira.entity.JiraConnection;
import org.forgeiq.planning.dto.*;
import org.forgeiq.planning.entity.Breakdown;
import org.forgeiq.planning.entity.Epic;
import org.forgeiq.planning.repository.BreakdownRepository;
import org.forgeiq.project.entity.Project;
import org.forgeiq.project.mapper.ProjectMapper;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlanningService {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;
    private final BreakdownPersistenceService breakdownPersistenceService;
    private final BreakdownRepository breakdownRepository;
    private final ProjectMapper projectMapper;

    private BreakdownListItemDto toListItem(Breakdown breakdown) {
        return BreakdownListItemDto.builder()
                .breakdownId(breakdown.getId())
                .title(breakdown.getTitle())
                .projectName(breakdown.getProject().getName())
                .description(breakdown.getDescription())
                .additionalContext(breakdown.getAdditionalContext())
                .updatedAt(breakdown.getUpdatedAt())
                .breakdownStatus(breakdown.getStatus())
                .build();
    }

    public Long breakdownRequirement(BreakdownRequestDto requirement, Long userId) {

        String prompt = """
                You are an expert Senior Product Manager, Enterprise Solution Architect, Agile Delivery Lead, and Technical Architect.
                
                Your responsibility is to analyze a business requirement and produce a production-ready Agile delivery plan that can be imported directly into Jira.
                
                ==================================================
                INPUTS
                ==================================================
                
                HIGH LEVEL INITIATIVE TITLE:
                %s
                
                DETAILED REQUIREMENT:
                %s
                
                ADDITIONAL CONTEXT:
                %s
                
                ==================================================
                INSTRUCTIONS
                ==================================================
                
                1. Treat the HIGH LEVEL INITIATIVE TITLE as the business initiative.
                2. Treat the DETAILED REQUIREMENT as the primary source of truth.
                3. Use the ADDITIONAL CONTEXT only to enrich or clarify the requirement.
                4. Infer missing technical details using software engineering best practices.
                5. Break the initiative into logical, independently deliverable increments.
                6. Create exactly ONE Epic.
                7. Create between THREE and TEN User Stories.
                8. Every Story must contain between TWO and EIGHT Subtasks.
                9. Ensure every Story delivers independent business value.
                10. Avoid duplicate stories, duplicate subtasks, or overlapping responsibilities.
                
                ==================================================
                EPIC RULES
                ==================================================
                
                Generate exactly one Epic.
                
                The Epic must contain:
                
                - jiraIssueKey
                  Format:
                  EPIC-001
                
                - title
                  Short, business focused and action oriented.
                
                - description
                  Should summarize:
                  - Business objective
                  - Scope
                  - Expected outcome
                  - Primary capabilities delivered
                
                - storyPoints
                  Estimate the overall effort using only:
                  1, 2, 3, 5, 8, or 13
                
                - acceptanceCriteria
                
                  Generate between THREE and SIX acceptance criteria.
                
                  Acceptance criteria must:
                
                  - Be business focused
                  - Be measurable
                  - Be testable
                  - Describe when the Epic is considered complete
                
                ==================================================
                USER STORY RULES
                ==================================================
                
                Generate between THREE and TEN User Stories.
                
                Stories should:
                
                - Represent complete vertical slices
                - Be independently deliverable
                - Be ordered logically
                - Deliver incremental business value
                
                Every Story must contain:
                
                jiraIssueKey
                
                Format:
                
                US-001
                US-002
                US-003
                ...
                
                title
                
                Keep concise and action oriented.
                
                description
                
                Must strictly follow:
                
                "As a <user>, I want <goal>, so that <benefit>"
                
                storyPoints
                
                Estimate using ONLY:
                
                1
                2
                3
                5
                8
                13
                
                acceptanceCriteria
                
                Generate between THREE and SIX acceptance criteria.
                
                Acceptance criteria must be:
                
                - Specific
                - Testable
                - Measurable
                - User focused
                
                ==================================================
                SUBTASK RULES
                ==================================================
                
                Every Story must contain between TWO and EIGHT implementation Subtasks.
                
                A Subtask represents a single engineering activity.
                
                Every Subtask must contain:
                
                - jiraIssueKey
                
                  Format:
                
                  TASK-001
                  TASK-002
                  TASK-003
                  ...
                
                - title
                
                  Short implementation focused title.
                
                - description
                
                  Detailed implementation description describing exactly what needs to be built.
                
                - acceptanceCriteria
                
                  A technical completion criterion describing when the subtask is complete.
                
                Examples:
                
                ✔ Create Story entity and repository
                
                ✔ Implement authentication middleware
                
                ✔ Build Login page UI
                
                ✔ Add API validation
                
                ✔ Configure OKTA callback endpoint
                
                ✔ Write integration tests
                
                ==================================================
                ESTIMATION GUIDELINES
                ==================================================
                
                Story Points:
                
                1 = trivial change
                
                2 = very small implementation
                
                3 = small feature
                
                5 = medium complexity
                
                8 = large feature involving multiple components
                
                13 = highly complex implementation
                
                Never use any other values.
                
                ==================================================
                QUALITY RULES
                ==================================================
                
                Ensure that:
                
                - Story titles are unique
                
                - Subtask titles are unique within each Story
                
                - Stories collectively satisfy the business requirement
                
                - Acceptance criteria validate the Story behavior
                
                - Subtask acceptance criteria validate technical implementation
                
                - The breakdown represents a realistic Agile delivery plan
                
                - All generated content is suitable for direct Jira import
                
                ==================================================
                OUTPUT RULES
                ==================================================
                
                Return ONLY valid JSON.
                
                Do NOT return:
                
                - Markdown
                
                - Code fences
                
                - Comments
                
                - Explanations
                
                - Additional text
                
                The JSON must be directly deserializable.
                
                ==================================================
                REQUIRED JSON SCHEMA
                ==================================================
                
                {
                  "epic": {
                    "jiraIssueKey": "",
                    "title": "",
                    "description": "",
                    "storyPoints": 0,
                    "acceptanceCriteria": [
                      ""
                    ]
                  },
                  "stories": [
                    {
                      "jiraIssueKey": "",
                      "title": "",
                      "description": "",
                      "storyPoints": 0,
                      "acceptanceCriteria": [
                        ""
                      ],
                      "subtasks": [
                        {
                          "jiraIssueKey": "",
                          "title": "",
                          "description": "",
                          "acceptanceCriteria": ""
                        }
                      ]
                    }
                  ]
                }
                """
                .formatted(
                        requirement.getTitle(),
                        requirement.getDescription(),
                        requirement.getAdditionalContext() == null
                                ? "None"
                                : requirement.getAdditionalContext()
                );

        String response = geminiService.generate(prompt);


        try {
            String cleaned = response
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();
            BreakdownResponseDto breakdown = objectMapper.readValue(
                    cleaned,
                    BreakdownResponseDto.class
            );
            System.out.println("Ress" + breakdown);
            return breakdownPersistenceService.saveBreakdown(breakdown, requirement, userId);
        } catch (Exception e) {
            System.err.println("Failed to parse Gemini response:");
            System.err.println(response);

            throw new RuntimeException(
                    "Invalid JSON returned by Gemini",
                    e
            );
        }
    }

    public BreakdownDashboardResponseDto getBreakdowns(Long userId) {

        List<Breakdown> entities = breakdownRepository.findByProject_User_IdOrderByUpdatedAtDesc(userId);

        BreakdownOverviewDto summary = BreakdownOverviewDto.builder()
                .totalBreakdown(entities.size())
                .drafts(entities.stream().filter(b -> b.getStatus() == BreakdownStatus.DRAFT).count())
                .generated(entities.stream().filter(b -> b.getStatus() == BreakdownStatus.GENERATED).count())
                .outOfSync(entities.stream().filter(b -> b.getStatus() == BreakdownStatus.OUT_OF_SYNC).count())
                .jiraSynced(entities.stream().filter(b -> b.getStatus() == BreakdownStatus.SYNCED).count())
                .build();

        List<BreakdownListItemDto> items = entities.stream().map(this::toListItem).toList();
        BreakdownsDto content = BreakdownsDto.builder()
                .items(items)
                .page(0)
                .pageSize(items.size())
                .totalElements(items.size())
                .totalPages(1)
                .build();
        return BreakdownDashboardResponseDto.builder()
                .summary(summary)
                .table(content)
                .build();
    }

    public BreakdownDetailResponseDto getBreakdownDetails(Long breakdownId, Long userId) {
        Breakdown breakdown = breakdownRepository.findByIdAndProject_User_Id(breakdownId, userId).orElseThrow(() -> new RuntimeException("Breakdown not found"));
        Project project = breakdown.getProject();

        User user = project.getUser();


        if (breakdown.getStatus() == BreakdownStatus.DRAFT) {
            return buildDraftResponse(breakdown, project, user);
        }

        List<Epic> epics = breakdown.getEpics();
        String breakdownUrl = buildBreakdownUrl(breakdown);

        int epicsCount = epics.size();

        int storiesCount = breakdown.getEpics().stream()
                .mapToInt(epic -> epic.getStories().size())
                .sum();

        int technicalTasksCount = breakdown.getEpics().stream()
                .mapToInt(epic -> epic.getStories().stream().mapToInt(story -> story.getSubtasks().size()).sum()).sum();

        int totalStoryPoints = breakdown.getEpics().stream()
                .mapToInt(epic -> epic.getStories().stream().mapToInt(story -> story.getStoryPoints()).sum()).sum();

        return BreakdownDetailResponseDto.builder()
                .id(breakdown.getId())
                .title(breakdown.getTitle())
                .description(breakdown.getDescription())
                .additionalContext(breakdown.getAdditionalContext())
                .breakdownUrl(breakdownUrl)
                .status(breakdown.getStatus())
                .epicsCount(epicsCount)
                .storiesCount(storiesCount)
                .totalStoryPoints(totalStoryPoints)
                .technicalTasksCount(technicalTasksCount)
                .project(projectMapper.toDto(breakdown.getProject()))
                .createdBy(user.getFirstName())
                .createdAt(breakdown.getCreatedAt())
                .updatedAt(breakdown.getUpdatedAt())
                .build();
    }

    private BreakdownDetailResponseDto buildDraftResponse(
            Breakdown breakdown,
            Project project,
            User user
    ) {
        return BreakdownDetailResponseDto.builder()
                .id(breakdown.getId())
                .title(breakdown.getTitle())
                .description(breakdown.getDescription())
                .additionalContext(breakdown.getAdditionalContext())
                .status(breakdown.getStatus())
                .breakdownUrl(null)
                .epicsCount(0)
                .storiesCount(0)
                .technicalTasksCount(0)
                .totalStoryPoints(0)
                .project(projectMapper.toDto(project))
                .createdBy(user.getFirstName())
                .createdAt(breakdown.getCreatedAt())
                .updatedAt(breakdown.getUpdatedAt())
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
