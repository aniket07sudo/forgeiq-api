package org.forgeiq.planning.repository;

import org.forgeiq.planning.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByStory_Epic_Breakdown_Id(Long breakdownId);
}
