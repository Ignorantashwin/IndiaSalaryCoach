package com.indiasalarycoach.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indiasalarycoach.dto.request.OfferComparisonRequest;
import com.indiasalarycoach.entity.OfferAnalysis;
import com.indiasalarycoach.entity.User;
import com.indiasalarycoach.exception.PlanLimitException;
import com.indiasalarycoach.exception.ResourceNotFoundException;
import com.indiasalarycoach.repository.OfferAnalysisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {

    private final OfferAnalysisRepository offerAnalysisRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Transactional
    public Map<String, Object> analyzeOffers(OfferComparisonRequest req, String email) {
        User user = userService.getByEmail(email);
        if (user.getPlan() == User.Plan.FREE) {
            throw new PlanLimitException("Offer comparison requires Pro or Premium plan.");
        }

        List<Map<String, Object>> scoredOffers = req.getOffers().stream().map(offer -> {
            double total = (offer.getBaseSalary() != null ? offer.getBaseSalary() : 0)
                    + (offer.getAnnualBonus() != null ? offer.getAnnualBonus() : 0)
                    + (offer.getEquityValue() != null ? offer.getEquityValue() / 4.0 : 0);

            // Scoring: total comp 50%, growth potential 25%, location 15%, remote 10%
            double score = 0;
            score += Math.min(50, (total / 3000000.0) * 50);
            score += scoreGrowth(offer.getGrowthPotential()) * 25;
            score += scoreCityDemand(offer.getCity()) * 15;
            if (Boolean.TRUE.equals(offer.getRemote())) score += 10;

            List<String> pros = new ArrayList<>();
            List<String> cons = new ArrayList<>();
            if (total > 2000000) pros.add("High total compensation package");
            if ("high".equalsIgnoreCase(offer.getGrowthPotential())) pros.add("Strong career growth potential");
            if (Boolean.TRUE.equals(offer.getRemote())) pros.add("Remote work flexibility");
            if (offer.getAnnualBonus() != null && offer.getAnnualBonus() > 100000) pros.add("Significant annual bonus");
            if (total < 800000) cons.add("Below market compensation");
            if ("low".equalsIgnoreCase(offer.getGrowthPotential())) cons.add("Limited growth prospects");
            if (!Boolean.TRUE.equals(offer.getRemote())) cons.add("No remote work option");

            Map<String, Object> scored = new LinkedHashMap<>();
            scored.put("companyName", offer.getCompanyName());
            scored.put("jobTitle", offer.getJobTitle());
            scored.put("baseSalary", offer.getBaseSalary());
            scored.put("annualBonus", offer.getAnnualBonus());
            scored.put("equityValue", offer.getEquityValue());
            scored.put("city", offer.getCity());
            scored.put("remote", offer.getRemote());
            scored.put("totalCompensation", Math.round(total * 100.0) / 100.0);
            scored.put("score", Math.round(score * 10.0) / 10.0);
            scored.put("pros", pros);
            scored.put("cons", cons);
            scored.put("marketComparison", total > 1500000 ? "Above market" : total > 900000 ? "At market" : "Below market");
            return scored;
        }).collect(Collectors.toList());

        int topPickIdx = 0;
        double maxScore = 0;
        for (int i = 0; i < scoredOffers.size(); i++) {
            double s = ((Number) scoredOffers.get(i).get("score")).doubleValue();
            if (s > maxScore) { maxScore = s; topPickIdx = i; }
        }

        String topCompany = (String) scoredOffers.get(topPickIdx).get("companyName");
        String recommendation = "Based on our analysis, " + topCompany + " offers the best overall package. " +
                "The combination of total compensation, growth trajectory, and work flexibility makes it the strongest choice.";

        try {
            String offersJson = objectMapper.writeValueAsString(scoredOffers);
            OfferAnalysis analysis = OfferAnalysis.builder()
                    .user(user).offersJson(offersJson).recommendation(recommendation).topPickIndex(topPickIdx)
                    .build();
            analysis = offerAnalysisRepository.save(analysis);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("id", analysis.getId());
            result.put("userId", user.getId());
            result.put("offers", scoredOffers);
            result.put("recommendation", recommendation);
            result.put("topPickIndex", topPickIdx);
            result.put("createdAt", analysis.getCreatedAt());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save analysis", e);
        }
    }

    public List<Map<String, Object>> getOffers(String email) {
        User user = userService.getByEmail(email);
        return offerAnalysisRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(a -> {
                    try {
                        List<?> offers = objectMapper.readValue(a.getOffersJson(), List.class);
                        Map<String, Object> r = new LinkedHashMap<>();
                        r.put("id", a.getId()); r.put("userId", user.getId());
                        r.put("offers", offers); r.put("recommendation", a.getRecommendation());
                        r.put("topPickIndex", a.getTopPickIndex()); r.put("createdAt", a.getCreatedAt());
                        return r;
                    } catch (Exception e) { return null; }
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Map<String, Object> getOffer(Long id, String email) {
        User user = userService.getByEmail(email);
        OfferAnalysis a = offerAnalysisRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Offer analysis not found"));
        try {
            List<?> offers = objectMapper.readValue(a.getOffersJson(), List.class);
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("id", a.getId()); r.put("userId", user.getId());
            r.put("offers", offers); r.put("recommendation", a.getRecommendation());
            r.put("topPickIndex", a.getTopPickIndex()); r.put("createdAt", a.getCreatedAt());
            return r;
        } catch (Exception e) { throw new RuntimeException("Failed to parse offer", e); }
    }

    @Transactional
    public void deleteOffer(Long id, String email) {
        User user = userService.getByEmail(email);
        OfferAnalysis a = offerAnalysisRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Offer analysis not found"));
        offerAnalysisRepository.delete(a);
    }

    private double scoreGrowth(String g) {
        if ("high".equalsIgnoreCase(g)) return 1.0;
        if ("medium".equalsIgnoreCase(g)) return 0.6;
        return 0.2;
    }

    private double scoreCityDemand(String city) {
        if (city == null) return 0.5;
        return switch (city.toLowerCase()) {
            case "bangalore", "bengaluru" -> 1.0;
            case "hyderabad", "pune" -> 0.9;
            case "mumbai", "delhi", "gurgaon" -> 0.85;
            default -> 0.7;
        };
    }
}
