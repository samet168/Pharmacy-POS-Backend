package com.pharmacy.pos.iam.entity;

import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.branch.entity.Device;
import com.pharmacy.pos.common.TimestampEntity;
import com.pharmacy.pos.common.enums.ShiftStatus;
import com.pharmacy.pos.iam.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "shifts")
public class Shift extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @Column(name = "opening_cash", nullable = false, precision = 12, scale = 2)
    private BigDecimal openingCash;

    @Column(name = "expected_cash", precision = 12, scale = 2)
    private BigDecimal expectedCash;

    @Column(name = "actual_cash", precision = 12, scale = 2)
    private BigDecimal actualCash;

    @Column(precision = 12, scale = 2)
    private BigDecimal difference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShiftStatus status = ShiftStatus.OPEN;

    @Column(name = "opened_at", nullable = false)
    private LocalDateTime openedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;
}
