package com.pharmacy.pos.tenant.entity;

import com.pharmacy.pos.common.BaseEntity;
import com.pharmacy.pos.common.enums.SubscriptionPlanStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "plan_name", nullable = false)
    private String planName;

    @Column(name = "max_branches")
    private Integer maxBranches;

    @Column(name = "max_users")
    private Integer maxUsers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionPlanStatus status = SubscriptionPlanStatus.TRIAL;

    @Column(name = "starts_at")
    private LocalDate startsAt;

    @Column(name = "ends_at")
    private LocalDate endsAt;
}
