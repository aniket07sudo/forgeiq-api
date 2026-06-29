package org.forgeiq.jira.entity;

import jakarta.persistence.*;
import lombok.*;
import org.forgeiq.auth.entity.User;
import org.forgeiq.project.entity.Project;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jira_connections")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JiraConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "base_url", nullable = false)
    private String baseUrl;

    @Column(nullable = false)
    private String email;

    @Column(name = "api_token", nullable = false)
    private String apiToken;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "jiraConnection")
    private List<Project> projects;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}