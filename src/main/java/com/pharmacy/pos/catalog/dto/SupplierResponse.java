package com.pharmacy.pos.catalog.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SupplierResponse {
    private Long id;
    private Long organizationId;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String taxId;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}