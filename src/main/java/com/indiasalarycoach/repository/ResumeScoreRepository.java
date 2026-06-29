package com.indiasalarycoach.repository;

import com.indiasalarycoach.entity.ResumeScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeScoreRepository extends JpaRepository<ResumeScore, Long> {
    List<ResumeScore> findByResumeIdOrderByScoredAtDesc(Long resumeId);
    Optional<ResumeScore> findFirstByResumeIdOrderByScoredAtDesc(Long resumeId);
}
