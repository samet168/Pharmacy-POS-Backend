package com.pharmacy.pos.iam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionRequest {
    @NotBlank
    private String code;

    private String description;
}
