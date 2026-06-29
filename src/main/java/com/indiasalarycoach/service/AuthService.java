package com.indiasalarycoach.service;

import com.indiasalarycoach.dto.request.LoginRequest;
import com.indiasalarycoach.dto.request.RegisterRequest;
import com.indiasalarycoach.dto.response.AuthResponse;
import com.indiasalarycoach.dto.response.UserProfileResponse;
import com.indiasalarycoach.entity.User;
import com.indiasalarycoach.exception.ConflictException;
import com.indiasalarycoach.exception.UnauthorizedException;
import com.indiasalarycoach.repository.UserRepository;
import com.indiasalarycoach.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already registered");
        }

        // Build and persist the user first so we have a valid DB-assigned ID
        User user = User.builder()
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName().trim())
                .role(User.Role.USER)
                .plan(User.Plan.FREE)
                .build();
        user = userRepository.save(user);

        // Generate tokens after the user is persisted
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        log.info("New user registered: {}", user.getEmail());
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserProfileResponse.from(user))
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase().trim(),
                        request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());
        user.setRefreshToken(refreshToken);
        user.setLastActiveAt(Instant.now());
        userRepository.save(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserProfileResponse.from(user))
                .build();
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.isTokenValid(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }
        String email = jwtTokenProvider.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new UnauthorizedException("Refresh token has been invalidated");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(email);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .user(UserProfileResponse.from(user))
                .build();
    }

    @Transactional
    public void logout(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setRefreshToken(null);
            userRepository.save(user);
        });
    }

    @Transactional
    public AuthResponse googleAuth(String idToken) {
        String googleClientId = System.getenv("GOOGLE_CLIENT_ID");
        if (googleClientId == null || googleClientId.isBlank()) {
            throw new UnauthorizedException(
                "Google OAuth is not configured. Set the GOOGLE_CLIENT_ID environment variable.");
        }
        // Full Google token verification would be implemented here with google-auth-library
        throw new UnauthorizedException("Google OAuth requires GOOGLE_CLIENT_ID to be configured");
    }
}
