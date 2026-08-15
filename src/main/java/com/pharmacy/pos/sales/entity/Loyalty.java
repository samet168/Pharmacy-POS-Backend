package com.pharmacy.pos.sales.entity;

import com.pharmacy.pos.common.TimestampEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "loyalty_programs")
public class Loyalty extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "points_per_dollar", nullable = false)
    private BigDecimal pointsPerDollar;

    @Column(name = "redemption_rate", nullable = false)
    private BigDecimal redemptionRate; // Points to dollar conversion

    @Column(name = "min_points_to_redeem")
    private Integer minPointsToRedeem;

    @Column(name = "max_points_per_transaction")
    private Integer maxPointsPerTransaction;

    @Column(name = "points_expiry_months")
    private Integer pointsExpiryMonths;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "tier_based")
    private boolean tierBased = false;

    @Column(name = "silver_threshold")
    private Integer silverThreshold;

    @Column(name = "gold_threshold")
    private Integer goldThreshold;

    @Column(name = "platinum_threshold")
    private Integer platinumThreshold;

    @Column(name = "silver_multiplier")
    private BigDecimal silverMultiplier;

    @Column(name = "gold_multiplier")
    private BigDecimal goldMultiplier;

    @Column(name = "platinum_multiplier")
    private BigDecimal platinumMultiplier;
}
