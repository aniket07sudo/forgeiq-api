package org.forgeiq.project.Repository;

import org.forgeiq.planning.entity.TaskStatus;
import org.forgeiq.project.entity.Project;
import org.forgeiq.project.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectStatusRepository extends JpaRepository<ProjectStatus, Long> {
    Optional<ProjectStatus> findFirstByProjectOrderByPositionAsc(Project project);
}