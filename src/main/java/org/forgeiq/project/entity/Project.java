package org.forgeiq.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.forgeiq.auth.entity.User;
import org.forgeiq.jira.entity.JiraConnection;
import org.forgeiq.planning.entity.Breakdown;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "jira_project_key",
            unique = true,
            length = 50
    )
    private String jiraProjectKey;

    @Column(
            name = "jira_project_id",
            unique = true,
            length = 100
    )
    private String jiraProjectId;

    @Column(
            name = "jira_project_name",
            length = 100
    )
    private String jiraProjectName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jira_connection_id")
    private JiraConnection jiraConnection;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Breakdown> breakdowns = new ArrayList<>();

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;
}
