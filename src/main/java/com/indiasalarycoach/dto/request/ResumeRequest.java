package com.indiasalarycoach.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResumeRequest {
    @NotBlank private String title;
    @NotBlank private String templateId;
    private String personalInfo;
    private String summary;
    private String experience;
    private String education;
    private String skills;
    private String certifications;
    private String projects;
    private String targetRole;
}
