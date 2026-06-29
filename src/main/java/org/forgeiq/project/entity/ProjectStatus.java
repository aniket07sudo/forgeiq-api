package org.forgeiq.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_statuses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "status_key")
    private String statusKey;

    private String label;

    private Integer position;

    @Column(name = "is_final")
    private boolean isFinal;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
