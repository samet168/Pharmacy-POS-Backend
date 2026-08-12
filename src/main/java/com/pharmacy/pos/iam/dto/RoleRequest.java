package com.pharmacy.pos.iam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleRequest {
    private Long organizationId;

    @NotBlank
    private String name;

    private Boolean isSystemRole;
}
