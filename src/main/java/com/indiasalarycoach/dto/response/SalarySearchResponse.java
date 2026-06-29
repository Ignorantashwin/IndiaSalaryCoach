package com.indiasalarycoach.dto.response;

import com.indiasalarycoach.entity.SalarySearch;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class SalarySearchResponse {
    private Long id;
    private String jobTitle;
    private String city;
    private String industry;
    private Integer experienceYears;
    private String skills;
    private Double minSalary;
    private Double maxSalary;
    private Double medianSalary;
    private Instant createdAt;

    public static SalarySearchResponse from(SalarySearch search) {
        return SalarySearchResponse.builder()
                .id(search.getId())
                .jobTitle(search.getJobTitle())
                .city(search.getCity())
                .industry(search.getIndustry())
                .experienceYears(search.getExperienceYears())
                .skills(search.getSkills())
                .minSalary(search.getMinSalary())
                .maxSalary(search.getMaxSalary())
                .medianSalary(search.getMedianSalary())
                .createdAt(search.getCreatedAt())
                .build();
    }
}
