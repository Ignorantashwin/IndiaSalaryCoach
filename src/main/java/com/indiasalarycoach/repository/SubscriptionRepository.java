package com.indiasalarycoach.repository;

import com.indiasalarycoach.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findFirstByUserIdAndStatusOrderByCreatedAtDesc(Long userId, Subscription.Status status);
    List<Subscription> findByUserIdOrderByCreatedAtDesc(Long userId);
    long countByStatus(Subscription.Status status);

    @Query("SELECT s.planName, COUNT(s), SUM(p.amount) FROM Subscription s " +
           "LEFT JOIN Payment p ON p.planId = s.planId AND p.status = 'SUCCESS' " +
           "WHERE s.status = 'ACTIVE' GROUP BY s.planName")
    List<Object[]> getPlanRevenueSummary();

    @Query("SELECT s FROM Subscription s JOIN FETCH s.user ORDER BY s.createdAt DESC")
    List<Subscription> findAllWithUsers();
}
