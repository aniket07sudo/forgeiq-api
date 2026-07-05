package org.forgeiq.planning.repository;

import org.forgeiq.planning.entity.Epic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpicRepository extends JpaRepository<Epic, Long> {
    List<Epic> findAllByBreakdown_Id(Long breakdownId);
}
