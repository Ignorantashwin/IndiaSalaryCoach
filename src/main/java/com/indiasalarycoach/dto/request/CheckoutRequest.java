package com.indiasalarycoach.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckoutRequest {
    @NotBlank private String planId;
    @NotBlank private String billingCycle;
}
