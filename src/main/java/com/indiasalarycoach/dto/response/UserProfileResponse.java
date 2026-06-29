package com.indiasalarycoach.dto.response;

import com.indiasalarycoach.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String email;
    private String name;
    private String role;
    private String plan;
    private String avatarUrl;
    private String currentTitle;
    private String currentCity;
    private Integer yearsExperience;
    private String industry;
    private String skills;
    private Instant createdAt;

    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name().toLowerCase())
                .plan(user.getPlan().name().toLowerCase())
                .avatarUrl(user.getAvatarUrl())
                .currentTitle(user.getCurrentTitle())
                .currentCity(user.getCurrentCity())
                .yearsExperience(user.getYearsExperience())
                .industry(user.getIndustry())
                .skills(user.getSkills())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
