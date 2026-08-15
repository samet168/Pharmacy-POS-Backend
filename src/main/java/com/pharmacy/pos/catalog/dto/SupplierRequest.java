package com.pharmacy.pos.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SupplierRequest {
    @NotNull
    private Long organizationId;

    @NotBlank(message = "Supplier name is required")
    private String name;

    private String contactPerson;

    private String phone;

    private String email;

    private String address;

    private String taxId;

    private boolean active = true;

    public boolean isActive() {
        return active;
    }
}