package org.forgeiq.planning.repository;

import org.forgeiq.planning.entity.TaskStatus;
import org.forgeiq.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    Optional<TaskStatus> findFirstByProjectOrderByPositionAsc(Project project);
}
