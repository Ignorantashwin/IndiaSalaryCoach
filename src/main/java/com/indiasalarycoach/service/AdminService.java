package com.indiasalarycoach.service;

import com.indiasalarycoach.entity.Subscription;
import com.indiasalarycoach.entity.User;
import com.indiasalarycoach.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;
    private final SalarySearchRepository salarySearchRepository;

    public Map<String, Object> getStats() {
        long totalUsers = userRepository.count();
        long activeSubscriptions = subscriptionRepository.countByStatus(Subscription.Status.ACTIVE);
        Double totalRevenue = paymentRepository.getTotalRevenue();
        Double monthlyRevenue = paymentRepository.getRevenueSince(Instant.now().minus(30, ChronoUnit.DAYS));
        long proUsers = userRepository.countByPlan(User.Plan.PRO);
        long premiumUsers = userRepository.countByPlan(User.Plan.PREMIUM);
        long freeUsers = userRepository.countByPlan(User.Plan.FREE);
        long newUsersThisWeek = userRepository.countByCreatedAtAfter(Instant.now().minus(7, ChronoUnit.DAYS));
        long searchesToday = salarySearchRepository.countByCreatedAtAfter(Instant.now().minus(1, ChronoUnit.DAYS));

        List<Object[]> monthlyData = paymentRepository.getMonthlyRevenue();
        List<Map<String, Object>> revenueByMonth = monthlyData.stream().limit(12).map(row -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("month", row[0]); m.put("revenue", row[1] != null ? row[1] : 0);
            m.put("subscriptions", row[2] != null ? row[2] : 0);
            return m;
        }).collect(Collectors.toList());

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("activeSubscriptions", activeSubscriptions);
        stats.put("monthlyRevenue", monthlyRevenue != null ? monthlyRevenue : 0.0);
        stats.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        stats.put("salarySearchesToday", searchesToday);
        stats.put("newUsersThisWeek", newUsersThisWeek);
        stats.put("proUsers", proUsers);
        stats.put("premiumUsers", premiumUsers);
        stats.put("freeUsers", freeUsers);
        stats.put("revenueByMonth", revenueByMonth);
        return stats;
    }

    public Map<String, Object> getUsers(int page, int limit, String search) {
        Page<User> userPage;
        PageRequest pr = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        userPage = userRepository.findAll(pr);

        List<Map<String, Object>> users = userPage.getContent().stream().map(u -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", u.getId()); m.put("email", u.getEmail()); m.put("name", u.getName());
            m.put("role", u.getRole().name().toLowerCase()); m.put("plan", u.getPlan().name().toLowerCase());
            m.put("salarySearchCount", salarySearchRepository.countByUserId(u.getId()));
            m.put("resumeCount", 0); m.put("lastActiveAt", u.getLastActiveAt());
            m.put("createdAt", u.getCreatedAt());
            return m;
        }).collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("users", users); result.put("total", userPage.getTotalElements());
        result.put("page", page); result.put("limit", limit);
        return result;
    }

    public List<Map<String, Object>> getSubscriptions() {
        return subscriptionRepository.findAllWithUsers().stream().map(s -> {
            double amount = "PRO".equalsIgnoreCase(s.getPlanId()) ?
                    (s.getBillingCycle() == Subscription.BillingCycle.ANNUAL ? 4799 : 499) :
                    (s.getBillingCycle() == Subscription.BillingCycle.ANNUAL ? 9599 : 999);
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", s.getId());
            m.put("userEmail", s.getUser().getEmail()); m.put("userName", s.getUser().getName());
            m.put("planName", s.getPlanName()); m.put("status", s.getStatus().name().toLowerCase());
            m.put("billingCycle", s.getBillingCycle().name().toLowerCase());
            m.put("amount", amount); m.put("createdAt", s.getCreatedAt());
            return m;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> getRevenue() {
        Double totalRevenue = paymentRepository.getTotalRevenue();
        Double monthlyRevenue = paymentRepository.getRevenueSince(Instant.now().minus(30, ChronoUnit.DAYS));

        List<Object[]> planData = subscriptionRepository.getPlanRevenueSummary();
        List<Map<String, Object>> byPlan = planData.stream().map(row -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("planName", row[0]); m.put("subscribers", row[1]); m.put("revenue", row[2] != null ? row[2] : 0);
            return m;
        }).collect(Collectors.toList());

        List<Object[]> monthlyData = paymentRepository.getMonthlyRevenue();
        List<Map<String, Object>> byMonth = monthlyData.stream().limit(12).map(row -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("month", row[0]); m.put("revenue", row[1] != null ? row[1] : 0); m.put("subscriptions", row[2] != null ? row[2] : 0);
            return m;
        }).collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        result.put("monthlyRevenue", monthlyRevenue != null ? monthlyRevenue : 0.0);
        result.put("byPlan", byPlan); result.put("byMonth", byMonth);
        return result;
    }
}
