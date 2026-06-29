package org.forgeiq.planning.repository;

import org.forgeiq.planning.entity.Epic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpicRepository extends JpaRepository<Epic, Long> {

}
