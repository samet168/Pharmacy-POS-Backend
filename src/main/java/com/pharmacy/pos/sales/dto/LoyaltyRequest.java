package com.pharmacy.pos.sales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoyaltyRequest {
    @NotNull
    private Long organizationId;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private BigDecimal pointsPerDollar;

    @NotNull
    private BigDecimal redemptionRate;

    private Integer minPointsToRedeem;

    private Integer maxPointsPerTransaction;

    private Integer pointsExpiryMonths;

    private Boolean isActive = true;

    private Boolean tierBased = false;

    private Integer silverThreshold;

    private Integer goldThreshold;

    private Integer platinumThreshold;

    private BigDecimal silverMultiplier;

    private BigDecimal goldMultiplier;

    private BigDecimal platinumMultiplier;
}
