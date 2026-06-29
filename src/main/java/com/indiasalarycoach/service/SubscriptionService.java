package com.indiasalarycoach.service;

import com.indiasalarycoach.dto.request.CheckoutRequest;
import com.indiasalarycoach.entity.Payment;
import com.indiasalarycoach.entity.Subscription;
import com.indiasalarycoach.entity.User;
import com.indiasalarycoach.repository.PaymentRepository;
import com.indiasalarycoach.repository.SubscriptionRepository;
import com.indiasalarycoach.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;
    private final UserService userService;

    @Value("${razorpay.key-id:}") private String razorpayKeyId;
    @Value("${razorpay.key-secret:}") private String razorpayKeySecret;
    @Value("${plan.pro.price-monthly:499}") private double proMonthly;
    @Value("${plan.pro.price-annual:4799}") private double proAnnual;
    @Value("${plan.premium.price-monthly:999}") private double premiumMonthly;
    @Value("${plan.premium.price-annual:9599}") private double premiumAnnual;

    public List<Map<String, Object>> getPlans() {
        return List.of(
            buildPlan("free", "Free", 0, 0, List.of(
                "5 salary searches/month", "Basic dashboard", "Market overview"), false, 5),
            buildPlan("pro", "Pro", proMonthly, proAnnual, List.of(
                "Unlimited salary analysis", "Offer comparison (up to 5)", "Career insights",
                "Top cities & skill impact", "Industry comparisons", "Market trend data"), true, null),
            buildPlan("premium", "Premium", premiumMonthly, premiumAnnual, List.of(
                "Everything in Pro", "ATS Resume Builder", "AI Resume Optimization",
                "Resume Score Analysis", "Resume PDF Export", "AI Career Roadmap Generator",
                "Interview Preparation", "AI Career Coach chat"), false, null)
        );
    }

    private Map<String, Object> buildPlan(String id, String name, double monthly, double annual,
                                           List<String> features, boolean highlighted, Integer maxSearches) {
        Map<String, Object> plan = new LinkedHashMap<>();
        plan.put("id", id); plan.put("name", name);
        plan.put("monthlyPrice", monthly); plan.put("annualPrice", annual);
        plan.put("currency", "INR"); plan.put("features", features);
        plan.put("highlighted", highlighted); plan.put("maxSalarySearches", maxSearches);
        return plan;
    }

    public Optional<Subscription> getCurrentSubscription(String email) {
        User user = userService.getByEmail(email);
        return subscriptionRepository.findFirstByUserIdAndStatusOrderByCreatedAtDesc(
                user.getId(), Subscription.Status.ACTIVE);
    }

    @Transactional
    public Map<String, Object> createCheckout(CheckoutRequest req, String email) {
        double amount = getAmount(req.getPlanId(), req.getBillingCycle());
        String planName = req.getPlanId().substring(0, 1).toUpperCase() + req.getPlanId().substring(1);

        if (razorpayKeyId == null || razorpayKeyId.isEmpty()) {
            // Return mock order when Razorpay not configured
            Map<String, Object> order = new HashMap<>();
            order.put("orderId", "order_mock_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
            order.put("amount", amount * 100); // paise
            order.put("currency", "INR");
            order.put("keyId", "rzp_test_placeholder");
            order.put("planName", planName);
            return order;
        }

        // Real Razorpay order creation when key is configured
        // TODO: Call Razorpay API to create order
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", "order_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        order.put("amount", amount * 100);
        order.put("currency", "INR");
        order.put("keyId", razorpayKeyId);
        order.put("planName", planName);
        return order;
    }

    @Transactional
    public Subscription verifyAndActivate(Map<String, String> verificationData, String email) {
        User user = userService.getByEmail(email);
        String planId = verificationData.get("planId");
        String billingCycle = verificationData.get("billingCycle");
        String orderId = verificationData.get("razorpayOrderId");
        String paymentId = verificationData.get("razorpayPaymentId");
        String signature = verificationData.get("razorpaySignature");

        // Verify Razorpay signature if key is configured
        if (razorpayKeySecret != null && !razorpayKeySecret.isEmpty()) {
            boolean valid = verifyRazorpaySignature(orderId, paymentId, signature);
            if (!valid) throw new com.indiasalarycoach.exception.UnauthorizedException("Payment verification failed");
        }

        double amount = getAmount(planId, billingCycle);
        String planName = planId.substring(0, 1).toUpperCase() + planId.substring(1);

        // Record payment
        Payment payment = Payment.builder()
                .user(user).amount(amount).currency("INR")
                .status(Payment.Status.SUCCESS).planName(planName).planId(planId)
                .billingCycle(billingCycle).razorpayOrderId(orderId).razorpayPaymentId(paymentId)
                .razorpaySignature(signature).build();
        paymentRepository.save(payment);

        // Cancel existing subscription
        subscriptionRepository.findFirstByUserIdAndStatusOrderByCreatedAtDesc(
                user.getId(), Subscription.Status.ACTIVE)
                .ifPresent(s -> { s.setStatus(Subscription.Status.CANCELLED); subscriptionRepository.save(s); });

        // Activate new subscription
        Instant now = Instant.now();
        Instant end = "annual".equalsIgnoreCase(billingCycle) ? now.plus(365, ChronoUnit.DAYS) : now.plus(30, ChronoUnit.DAYS);
        Subscription sub = Subscription.builder()
                .user(user).planId(planId).planName(planName)
                .status(Subscription.Status.ACTIVE)
                .billingCycle("annual".equalsIgnoreCase(billingCycle) ? Subscription.BillingCycle.ANNUAL : Subscription.BillingCycle.MONTHLY)
                .currentPeriodStart(now).currentPeriodEnd(end).cancelAtPeriodEnd(false).build();
        sub = subscriptionRepository.save(sub);

        // Upgrade user plan
        user.setPlan("pro".equalsIgnoreCase(planId) ? User.Plan.PRO : User.Plan.PREMIUM);
        userRepository.save(user);

        return sub;
    }

    @Transactional
    public Subscription cancelSubscription(String email) {
        User user = userService.getByEmail(email);
        Subscription sub = subscriptionRepository.findFirstByUserIdAndStatusOrderByCreatedAtDesc(
                user.getId(), Subscription.Status.ACTIVE)
                .orElseThrow(() -> new com.indiasalarycoach.exception.ResourceNotFoundException("No active subscription"));
        sub.setCancelAtPeriodEnd(true);
        return subscriptionRepository.save(sub);
    }

    public List<Payment> getPaymentHistory(String email) {
        User user = userService.getByEmail(email);
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    private double getAmount(String planId, String cycle) {
        if ("pro".equalsIgnoreCase(planId)) return "annual".equalsIgnoreCase(cycle) ? proAnnual : proMonthly;
        if ("premium".equalsIgnoreCase(planId)) return "annual".equalsIgnoreCase(cycle) ? premiumAnnual : premiumMonthly;
        return 0;
    }

    private boolean verifyRazorpaySignature(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(razorpayKeySecret.getBytes(), "HmacSHA256"));
            byte[] hash = mac.doFinal(payload.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));
            return hex.toString().equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
}
