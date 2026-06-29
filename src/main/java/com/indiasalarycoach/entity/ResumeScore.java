package com.indiasalarycoach.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "resume_scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(name = "overall_score", nullable = false)
    private Double overallScore;

    @Column(name = "ats_score", nullable = false)
    private Double atsScore;

    @Column(name = "keyword_score", nullable = false)
    private Double keywordScore;

    @Column(name = "format_score", nullable = false)
    private Double formatScore;

    @Column(name = "content_score", nullable = false)
    private Double contentScore;

    @Column(name = "missing_keywords", columnDefinition = "TEXT")
    private String missingKeywords;

    @Column(name = "matched_keywords", columnDefinition = "TEXT")
    private String matchedKeywords;

    @Column(columnDefinition = "TEXT")
    private String suggestions;

    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;

    @CreationTimestamp
    @Column(name = "scored_at", updatable = false)
    private Instant scoredAt;
}
