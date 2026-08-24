package com.pharmacy.pos.tenant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionCheckoutRequest {

    @NotNull(message = "Organization ID is required")
    private Long organizationId;

    @NotBlank(message = "Plan name is required")
    private String planName;

    private String billingCycle = "MONTHLY"; // MONTHLY or YEARLY

    private Integer maxBranches = 5;

    private Integer maxUsers = 20;

    private String paymentMethod = "MOCK_STRIPE";

    private String paymentToken;
}
