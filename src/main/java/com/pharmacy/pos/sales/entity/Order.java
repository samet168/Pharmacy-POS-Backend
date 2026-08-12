package com.pharmacy.pos.sales.entity;

import com.pharmacy.pos.common.BaseEntity;
import com.pharmacy.pos.common.enums.OrderStatus;
import com.pharmacy.pos.common.enums.SyncStatus;
import com.pharmacy.pos.customer.entity.Customer;
import com.pharmacy.pos.customer.entity.Prescription;
import com.pharmacy.pos.iam.entity.Shift;
import com.pharmacy.pos.iam.entity.User;
import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.branch.entity.Device;
import com.pharmacy.pos.tenant.entity.Organization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String clientUuid;

    @Column(unique = true, nullable = false)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @Column(name = "prescription_url")
    private String prescriptionUrl;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 12, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal grandTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.COMPLETED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SyncStatus syncStatus = SyncStatus.SYNCED;

    @Column(name = "created_at_device")
    private LocalDateTime createdAtDevice;
}
