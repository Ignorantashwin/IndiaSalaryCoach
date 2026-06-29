package com.indiasalarycoach.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SalaryResultResponse {
    private String jobTitle;
    private String city;
    private String industry;
    private Integer experienceYears;
    private Double minSalary;
    private Double maxSalary;
    private Double medianSalary;
    private Double marketAverage;
    private String currency;
    private Integer dataPoints;
    private String dataSource;
    private String lastUpdated;
    private List<CityInsightDto> topCities;
    private List<SkillImpactDto> skillImpacts;
    private List<IndustryComparisonDto> industryComparisons;
    private List<PercentileRankDto> percentileRanks;

    @Data
    @Builder
    public static class CityInsightDto {
        private String city;
        private Double averageSalary;
        private Double demandScore;
        private Double costOfLivingIndex;
        private Double salaryToLivingRatio;
    }

    @Data
    @Builder
    public static class SkillImpactDto {
        private String skill;
        private Double percentageIncrease;
        private Double absoluteIncrease;
        private String demandLevel;
    }

    @Data
    @Builder
    public static class IndustryComparisonDto {
        private String industry;
        private Double averageSalary;
        private Double growthRate;
        private String demandTrend;
    }

    @Data
    @Builder
    public static class PercentileRankDto {
        private Integer percentile;
        private Double salary;
    }
}
