package org.forgeiq.planning.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.forgeiq.common.enums.BreakdownStatus;
import org.forgeiq.planning.dto.BreakdownRequestDto;
import org.forgeiq.planning.entity.Breakdown;
import org.forgeiq.planning.repository.BreakdownRepository;
import org.forgeiq.project.Repository.ProjectRepository;
import org.forgeiq.project.entity.Project;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BreakdownDraftService {

    private final ProjectRepository projectRepository;
    private final BreakdownRepository breakdownRepository;

    public Long saveDraft(BreakdownRequestDto request) {
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new RuntimeException("Project not Found"));

        Breakdown breakdown = Breakdown.builder()
                .project(project)
                .title(request.getTitle())
                .description(request.getDescription())
                .additionalContext(request.getAdditionalContext())
                .status(BreakdownStatus.DRAFT)
                .build();

        return breakdownRepository.save(breakdown).getId();
    }
}
