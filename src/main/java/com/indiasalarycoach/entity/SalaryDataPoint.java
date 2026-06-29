package com.indiasalarycoach.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "salary_data_points",
       indexes = {
           @Index(name = "idx_sdp_job_title", columnList = "job_title"),
           @Index(name = "idx_sdp_city", columnList = "city"),
           @Index(name = "idx_sdp_industry", columnList = "industry"),
           @Index(name = "idx_sdp_experience", columnList = "experience_min,experience_max")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryDataPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "industry", nullable = false)
    private String industry;

    @Column(name = "experience_min", nullable = false)
    private Integer experienceMin;

    @Column(name = "experience_max", nullable = false)
    private Integer experienceMax;

    @Column(name = "salary_min", nullable = false)
    private Double salaryMin;

    @Column(name = "salary_max", nullable = false)
    private Double salaryMax;

    @Column(name = "salary_median", nullable = false)
    private Double salaryMedian;

    @Column(nullable = false)
    private String currency;

    @Column(name = "data_source")
    private String dataSource;

    @Column(name = "sample_size")
    private Integer sampleSize;

    @Column(name = "skills_required", columnDefinition = "TEXT")
    private String skillsRequired;

    @Column(name = "reported_year")
    private Integer reportedYear;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
