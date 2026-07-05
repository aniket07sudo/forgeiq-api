package org.forgeiq.planning.repository;

import org.forgeiq.planning.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findAllByEpic_Breakdown_Id(Long epicId);
}
