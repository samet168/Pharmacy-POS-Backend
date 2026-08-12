package com.pharmacy.pos.iam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PinLoginRequest {
    @NotBlank
    private String pinCode;

    @NotNull
    private Long branchId;

    private String deviceUuid;
}
