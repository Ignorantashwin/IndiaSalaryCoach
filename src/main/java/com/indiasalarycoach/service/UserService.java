package com.indiasalarycoach.service;

import com.indiasalarycoach.dto.response.SalarySearchResponse;
import com.indiasalarycoach.dto.response.UserProfileResponse;
import com.indiasalarycoach.entity.User;
import com.indiasalarycoach.exception.ResourceNotFoundException;
import com.indiasalarycoach.repository.CareerReportRepository;
import com.indiasalarycoach.repository.OfferAnalysisRepository;
import com.indiasalarycoach.repository.ResumeRepository;
import com.indiasalarycoach.repository.SalarySearchRepository;
import com.indiasalarycoach.repository.SubscriptionRepository;
import com.indiasalarycoach.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SalarySearchRepository salarySearchRepository;
    private final ResumeRepository resumeRepository;
    private final CareerReportRepository careerReportRepository;
    private final OfferAnalysisRepository offerAnalysisRepository;
    private final SubscriptionRepository subscriptionRepository;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserProfileResponse getProfile(String email) {
        return UserProfileResponse.from(getByEmail(email));
    }

    @Transactional
    public UserProfileResponse updateProfile(String email, Map<String, Object> updates) {
        User user = getByEmail(email);
        if (updates.containsKey("name")) user.setName((String) updates.get("name"));
        if (updates.containsKey("currentTitle")) user.setCurrentTitle((String) updates.get("currentTitle"));
        if (updates.containsKey("currentCity")) user.setCurrentCity((String) updates.get("currentCity"));
        if (updates.containsKey("industry")) user.setIndustry((String) updates.get("industry"));
        if (updates.containsKey("skills")) user.setSkills((String) updates.get("skills"));
        if (updates.containsKey("yearsExperience")) {
            Object val = updates.get("yearsExperience");
            if (val instanceof Integer i) user.setYearsExperience(i);
            else if (val instanceof Number n) user.setYearsExperience(n.intValue());
        }
        return UserProfileResponse.from(userRepository.save(user));
    }

    public Map<String, Object> getDashboard(String email) {
        User user = getByEmail(email);
        long searchCount = salarySearchRepository.countByUserId(user.getId());
        long resumeCount = resumeRepository.countByUserId(user.getId());
        long reportCount = careerReportRepository.countByUserId(user.getId());
        long offerCount = offerAnalysisRepository.countByUserId(user.getId());

        List<SalarySearchResponse> recentSearches = salarySearchRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .limit(5)
                .map(SalarySearchResponse::from)
                .toList();

        var subscription = subscriptionRepository
                .findFirstByUserIdAndStatusOrderByCreatedAtDesc(user.getId(),
                        com.indiasalarycoach.entity.Subscription.Status.ACTIVE);

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("salarySearchCount", searchCount);
        dashboard.put("resumeCount", resumeCount);
        dashboard.put("careerReportCount", reportCount);
        dashboard.put("offerCount", offerCount);
        dashboard.put("plan", user.getPlan().name().toLowerCase());
        dashboard.put("recentSearches", recentSearches);
        dashboard.put("latestResumeScore", null);
        dashboard.put("subscriptionStatus", subscription.map(s -> s.getStatus().name().toLowerCase()).orElse(null));
        dashboard.put("subscriptionExpiresAt", subscription.map(s ->
                s.getCurrentPeriodEnd() != null ? s.getCurrentPeriodEnd().toString() : null).orElse(null));
        return dashboard;
    }
}
