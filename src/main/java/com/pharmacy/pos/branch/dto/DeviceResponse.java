package com.pharmacy.pos.branch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    // Rename field from "isActive" to "active" to fix the Lombok double-prefix getter bug.
    // @JsonProperty keeps the JSON wire name as "isActive" so clients are not affected.
    @JsonProperty("isActive")
    private Boolean active;

    private LocalDateTime registeredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
