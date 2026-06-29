package org.forgeiq.jira.repository;

import org.forgeiq.jira.entity.JiraConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JiraConnectionRepository
        extends JpaRepository<JiraConnection, Long> {

    Optional<JiraConnection> findByIdAndUser_Id(
            Long id,
            Long userId
    );

    Optional<JiraConnection> findByUser_Id(
            Long userId
    );

    Optional<JiraConnection> findByUser_IdAndIsDefaultTrue(
            Long userId
    );

}