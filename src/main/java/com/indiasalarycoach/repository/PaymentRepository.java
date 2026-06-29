package com.indiasalarycoach.repository;

import com.indiasalarycoach.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Payment> findByRazorpayOrderId(String orderId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'SUCCESS'")
    Double getTotalRevenue();

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'SUCCESS' AND p.createdAt > :since")
    Double getRevenueSince(@Param("since") Instant since);

    @Query("""
        SELECT FUNCTION('TO_CHAR', p.createdAt, 'YYYY-MM') as month,
               SUM(p.amount) as revenue,
               COUNT(p) as subscriptions
        FROM Payment p
        WHERE p.status = 'SUCCESS'
        GROUP BY month
        ORDER BY month DESC
        """)
    List<Object[]> getMonthlyRevenue();
}
