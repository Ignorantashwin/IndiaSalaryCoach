package com.indiasalarycoach.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.indiasalarycoach.dto.request.ForgotPasswordRequest;
import com.indiasalarycoach.dto.request.ResetPasswordRequest;
import com.indiasalarycoach.entity.PasswordResetToken;
import com.indiasalarycoach.entity.User;
import com.indiasalarycoach.repository.PasswordResetTokenRepository;
import com.indiasalarycoach.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor
@Slf4j

public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository ;
    private final PasswordEncoder passwordEncoder;

    public void forgotPasswordRequest(ForgotPasswordRequest request){
    
       User user = userRepository.findByEmail(request.getEmail()). orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = UUID.randomUUID().toString();
         log.info("Reset Token : {}", token);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setUsed(false);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        passwordResetTokenRepository.save(resetToken);
    }

    public void resetPassword(ResetPasswordRequest request){
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken()).orElseThrow(() -> new RuntimeException("Invalid Token"));
        if (resetToken.isUsed()) {
            throw new RuntimeException("Token already used");
        }

         if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token eexpired");
        }
        if (request.getNewPassword().length()<8) {
            throw new RuntimeException("Password must be at least 8 characters");
        }
          User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        
        userRepository.save(user);
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

}
