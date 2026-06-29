package org.forgeiq.planning.repository;

import org.forgeiq.planning.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
}
