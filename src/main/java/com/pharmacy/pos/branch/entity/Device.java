package com.pharmacy.pos.branch.entity;

import com.pharmacy.pos.common.TimestampEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "devices")
public class Device extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(unique = true, nullable = false)
    private String deviceUuid;

    private String deviceName;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;
}