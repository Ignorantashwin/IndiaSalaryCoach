package com.indiasalarycoach.controller;

import com.indiasalarycoach.dto.request.ForgotPasswordRequest;
import com.indiasalarycoach.dto.request.LoginRequest;
import com.indiasalarycoach.dto.request.RegisterRequest;
import com.indiasalarycoach.dto.request.ResetPasswordRequest;
import com.indiasalarycoach.dto.response.AuthResponse;
import com.indiasalarycoach.service.AuthService;
import com.indiasalarycoach.service.PasswordResetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

     
    private final PasswordResetService passwordResetService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(authService.refreshToken(body.get("refreshToken")));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) authService.logout(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleAuth(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(authService.googleAuth(body.get("idToken")));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request ){
        passwordResetService.forgotPasswordRequest(request);
        return ResponseEntity.ok("Reset token generated");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request){
        passwordResetService.resetPassword(request);
        return ResponseEntity.ok("Password reset successful");
    }
    
}
