package com.pharmacy.pos.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyResponse {
    private Long id;
    private Long organizationId;
    private String name;
    private String description;
    private BigDecimal pointsPerDollar;
    private BigDecimal redemptionRate;
    private Integer minPointsToRedeem;
    private Integer maxPointsPerTransaction;
    private Integer pointsExpiryMonths;
    private boolean isActive;
    private boolean tierBased;
    private Integer silverThreshold;
    private Integer goldThreshold;
    private Integer platinumThreshold;
    private BigDecimal silverMultiplier;
    private BigDecimal goldMultiplier;
    private BigDecimal platinumMultiplier;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
