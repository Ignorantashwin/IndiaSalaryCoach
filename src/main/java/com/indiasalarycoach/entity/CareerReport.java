package com.indiasalarycoach.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "career_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "source_role", nullable = false)
    private String currentRole;

    @Column(name = "target_role", nullable = false)
    private String targetRole;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String roadmap;

    @Column(name = "skill_gaps", nullable = false, columnDefinition = "TEXT")
    private String skillGaps;

    @Column(name = "timeline_months", nullable = false)
    private Integer timelineMonths;

    @Column(name = "learning_resources", columnDefinition = "TEXT")
    private String learningResources;

    @Column(name = "salary_projection")
    private Double salaryProjection;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "input_skills", columnDefinition = "TEXT")
    private String inputSkills;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column
    private String industry;

    @Column
    private String city;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
