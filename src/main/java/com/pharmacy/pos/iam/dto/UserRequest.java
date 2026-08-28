package com.pharmacy.pos.iam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserRequest {
    @NotNull
    private Long organizationId;

    @NotNull
    private Long roleId;

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @jakarta.validation.constraints.Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String phone;

    private String pinCode;

    private String imageUrl;

    private Boolean isActive;

    private List<Long> branchIds;
}
