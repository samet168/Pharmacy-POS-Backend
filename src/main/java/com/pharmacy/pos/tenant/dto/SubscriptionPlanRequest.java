package com.pharmacy.pos.tenant.dto;

import com.pharmacy.pos.common.enums.SubscriptionPlanStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SubscriptionPlanRequest {
    @NotNull
    private Long organizationId;

    @NotBlank
    private String planName;

    private Integer maxBranches;

    private Integer maxUsers;

    private SubscriptionPlanStatus status;

    private LocalDate startsAt;

    private LocalDate endsAt;
}
