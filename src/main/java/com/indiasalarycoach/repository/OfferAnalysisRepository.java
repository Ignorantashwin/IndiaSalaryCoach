package com.indiasalarycoach.repository;

import com.indiasalarycoach.entity.OfferAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferAnalysisRepository extends JpaRepository<OfferAnalysis, Long> {
    List<OfferAnalysis> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<OfferAnalysis> findByIdAndUserId(Long id, Long userId);
    long countByUserId(Long userId);
}
