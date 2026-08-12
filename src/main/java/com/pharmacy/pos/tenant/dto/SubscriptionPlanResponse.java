package com.pharmacy.pos.tenant.dto;

import com.pharmacy.pos.common.enums.SubscriptionPlanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanResponse {
    private Long id;
    private Long organizationId;
    private String planName;
    private Integer maxBranches;
    private Integer maxUsers;
    private SubscriptionPlanStatus status;
    private LocalDate startsAt;
    private LocalDate endsAt;
    private LocalDateTime createdAt;
}
