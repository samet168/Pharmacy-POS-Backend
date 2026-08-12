package com.pharmacy.pos.branch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponse {
    private Long id;
    private Long branchId;
    private String deviceUuid;
    private String deviceName;
    private LocalDateTime lastSyncedAt;
    private boolean isActive;
    private LocalDateTime registeredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}