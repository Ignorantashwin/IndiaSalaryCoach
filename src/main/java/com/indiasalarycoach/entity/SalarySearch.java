package com.indiasalarycoach.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "salary_searches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalarySearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String industry;

    @Column(name = "experience_years", nullable = false)
    private Integer experienceYears;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(name = "min_salary", nullable = false)
    private Double minSalary;

    @Column(name = "max_salary", nullable = false)
    private Double maxSalary;

    @Column(name = "median_salary", nullable = false)
    private Double medianSalary;

    @Column(name = "result_json", columnDefinition = "TEXT")
    private String resultJson;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
