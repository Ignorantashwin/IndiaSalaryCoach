package com.indiasalarycoach.controller;

import com.indiasalarycoach.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getPlans() {
        return ResponseEntity.ok(subscriptionService.getPlans());
    }
}
