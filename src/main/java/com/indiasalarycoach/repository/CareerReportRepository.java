package com.indiasalarycoach.repository;

import com.indiasalarycoach.entity.CareerReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerReportRepository extends JpaRepository<CareerReport, Long> {
    List<CareerReport> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<CareerReport> findByIdAndUserId(Long id, Long userId);
    long countByUserId(Long userId);
}
