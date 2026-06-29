package org.forgeiq.planning.repository;

import org.forgeiq.planning.entity.Breakdown;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BreakdownRepository extends JpaRepository<Breakdown, Long> {
    List<Breakdown> findByProject_User_IdOrderByUpdatedAtDesc(long userId);

    Optional<Breakdown> findByIdAndProject_User_Id(Long breakdownId, Long userId);

    Optional<Breakdown> findByIdAndProject_JiraConnection_User_Id(
            Long breakdownId,
            Long userId
    );
}
