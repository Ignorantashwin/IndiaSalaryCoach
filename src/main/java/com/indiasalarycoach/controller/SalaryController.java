package com.indiasalarycoach.controller;

import com.indiasalarycoach.dto.request.SalarySearchRequest;
import com.indiasalarycoach.dto.response.SalaryResultResponse;
import com.indiasalarycoach.dto.response.SalarySearchResponse;
import com.indiasalarycoach.service.SalaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/salary")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;

    @PostMapping("/search")
    public ResponseEntity<SalaryResultResponse> searchSalary(
            @Valid @RequestBody SalarySearchRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(salaryService.searchSalary(req, userDetails.getUsername()));
    }

    @GetMapping("/searches")
    public ResponseEntity<List<SalarySearchResponse>> getSearches(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(salaryService.getSearchHistory(userDetails.getUsername()));
    }

    @GetMapping("/top-cities")
    public ResponseEntity<List<SalaryResultResponse.CityInsightDto>> getTopCities(
            @RequestParam String jobTitle,
            @RequestParam(required = false) String industry) {
        return ResponseEntity.ok(salaryService.getTopCities(jobTitle, industry));
    }

    @GetMapping("/skill-impact")
    public ResponseEntity<List<SalaryResultResponse.SkillImpactDto>> getSkillImpact(
            @RequestParam String jobTitle,
            @RequestParam String skills) {
        return ResponseEntity.ok(salaryService.getSkillImpact(jobTitle, skills));
    }

    @GetMapping("/industry-comparison")
    public ResponseEntity<List<SalaryResultResponse.IndustryComparisonDto>> getIndustryComparison(
            @RequestParam String jobTitle) {
        return ResponseEntity.ok(salaryService.getIndustryComparison(jobTitle));
    }

    @GetMapping("/market-trends")
    public ResponseEntity<List<Map<String, Object>>> getMarketTrends(
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String city) {
        return ResponseEntity.ok(salaryService.getMarketTrends(industry, city));
    }
}
