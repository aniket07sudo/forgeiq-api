package org.forgeiq.project.Repository;

import org.forgeiq.project.dto.ProjectDto;
import org.forgeiq.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);
    Optional<Project> findByIdAndUser_Id(Long id, Long userId);
    List<Project> findAllByUser_Id(Long userId);
    Optional<Project> findByIdAndJiraConnection_User_Id(
            Long projectId,
            Long userId
    );
}
