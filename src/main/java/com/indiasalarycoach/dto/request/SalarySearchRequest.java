package com.indiasalarycoach.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SalarySearchRequest {
    @NotBlank private String jobTitle;
    @NotNull @Min(0) private Integer experienceYears;
    private String skills;
    @NotBlank private String city;
    @NotBlank private String industry;
}
