package com.pharmacy.pos.branch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BranchRequest {
    @NotNull
    private Long organizationId;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String location;

    private String phone;
}
