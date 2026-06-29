package com.indiasalarycoach.controller;

import com.indiasalarycoach.dto.request.CareerReportRequest;
import com.indiasalarycoach.entity.CareerReport;
import com.indiasalarycoach.service.CareerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/career")
@RequiredArgsConstructor
public class CareerController {

    private final CareerService careerService;

    @GetMapping("/reports")
    public ResponseEntity<List<CareerReport>> getReports(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(careerService.getReports(userDetails.getUsername()));
    }

    @PostMapping("/reports")
    public ResponseEntity<CareerReport> generateReport(
            @Valid @RequestBody CareerReportRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(careerService.generateReport(req, userDetails.getUsername()));
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<CareerReport> getReport(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(careerService.getReport(id, userDetails.getUsername()));
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> careerChat(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(careerService.careerChat(
                body.get("message"), body.get("context"), userDetails.getUsername()));
    }

    @PostMapping("/interview-prep")
    public ResponseEntity<Map<String, Object>> getInterviewPrep(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(careerService.getInterviewPrep(
                body.get("targetRole"), body.get("industry"),
                body.get("skills"), body.get("experienceLevel"), userDetails.getUsername()));
    }

    @PostMapping("/interview-questions")
public ResponseEntity<String> generateInterviewQuestions(
        @RequestBody Map<String, String> body) {

    return ResponseEntity.ok(careerService.generateInterviewQuestions(
                    body.get("role"),
                    body.get("level")
            )
    );
}

@PostMapping("/salary-negotiation")
public ResponseEntity<String> generateNegotiationLetter(
        @RequestBody Map<String, String> body) {

    return ResponseEntity.ok(careerService.generateNegotiationLetter(
                    body.get("name"),
                    body.get("company"),
                    body.get("role"),
                    body.get("currentOffer"),
                    body.get("expectedSalary")
            )
    );
}
}
