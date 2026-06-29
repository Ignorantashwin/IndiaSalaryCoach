package com.indiasalarycoach.controller;

import com.indiasalarycoach.dto.request.CheckoutRequest;
import com.indiasalarycoach.entity.Payment;
import com.indiasalarycoach.entity.Subscription;
import com.indiasalarycoach.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentSubscription(@AuthenticationPrincipal UserDetails userDetails) {
        return subscriptionService.getCurrentSubscription(userDetails.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(null));
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> createCheckout(
            @Valid @RequestBody CheckoutRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.createCheckout(req, userDetails.getUsername()));
    }

    @PostMapping("/verify")
    public ResponseEntity<Subscription> verifyPayment(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.verifyAndActivate(body, userDetails.getUsername()));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Subscription> cancelSubscription(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(userDetails.getUsername()));
    }

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getPaymentHistory(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.getPaymentHistory(userDetails.getUsername()));
    }
}
