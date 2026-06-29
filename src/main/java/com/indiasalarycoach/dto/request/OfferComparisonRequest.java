package com.indiasalarycoach.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OfferComparisonRequest {
    @NotEmpty
    private List<JobOfferRequest> offers;

    @Data
    public static class JobOfferRequest {
        private String companyName;
        private String jobTitle;
        private Double baseSalary;
        private Double annualBonus;
        private Double equityValue;
        private String benefits;
        private String city;
        private String industry;
        private Boolean remote;
        private String growthPotential;
    }
}
