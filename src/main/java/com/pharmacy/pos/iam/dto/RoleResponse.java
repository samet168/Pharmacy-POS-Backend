package com.pharmacy.pos.iam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private Long id;
    private Long organizationId;
    private String name;
    private boolean systemRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
