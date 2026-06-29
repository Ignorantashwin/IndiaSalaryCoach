package com.indiasalarycoach.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.indiasalarycoach.entity.PasswordResetToken;
import com.indiasalarycoach.entity.User;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository <PasswordResetToken, Long> {
    Optional <PasswordResetToken> findByToken(String token);
    List<PasswordResetToken> findByUser(User user); 
    
}
