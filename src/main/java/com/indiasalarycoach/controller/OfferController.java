package com.indiasalarycoach.controller;

import com.indiasalarycoach.dto.request.OfferComparisonRequest;
import com.indiasalarycoach.service.OfferService;
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
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getOffers(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(offerService.getOffers(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOffer(
            @Valid @RequestBody OfferComparisonRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(offerService.analyzeOffers(req, userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOffer(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(offerService.getOffer(id, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        offerService.deleteOffer(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
