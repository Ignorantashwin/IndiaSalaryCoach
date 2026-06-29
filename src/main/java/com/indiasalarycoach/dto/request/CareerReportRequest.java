package com.indiasalarycoach.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CareerReportRequest {
    @NotBlank private String currentRole;
    @NotBlank private String targetRole;
    @NotBlank private String skills;
    @NotNull @Min(0) private Integer experienceYears;
    private String industry;
    private String city;
}
