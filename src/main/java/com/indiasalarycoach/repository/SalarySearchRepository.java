package com.indiasalarycoach.repository;

import com.indiasalarycoach.entity.SalarySearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SalarySearchRepository extends JpaRepository<SalarySearch, Long> {
    List<SalarySearch> findByUserIdOrderByCreatedAtDesc(Long userId);
    long countByUserId(Long userId);
    long countByCreatedAtAfter(Instant date);

    @Query("SELECT COUNT(s) FROM SalarySearch s WHERE s.user.id = :userId AND s.createdAt > :since")
    long countRecentSearches(@Param("userId") Long userId, @Param("since") Instant since);
}
