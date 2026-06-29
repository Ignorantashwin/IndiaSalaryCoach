package com.indiasalarycoach.repository;

import com.indiasalarycoach.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     Optional <User> findByEmail(String email);
    Optional<User> findByGoogleId(String googleId);
    boolean existsByEmail(String email);
    long countByPlan(User.Plan plan);
    long countByCreatedAtAfter(Instant date);

    @Query("SELECT COUNT(u) FROM User u WHERE u.lastActiveAt > :since")
    long countActiveUsersSince(Instant since);
}
