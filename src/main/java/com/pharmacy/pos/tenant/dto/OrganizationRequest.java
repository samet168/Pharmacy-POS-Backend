package com.pharmacy.pos.tenant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrganizationRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 3, max = 50)
    private String slug;

    private String licenseNumber;

    private String contactEmail;

    private String contactPhone;

    private String address;

    private String logoUrl;

    private String baseCurrency = "USD";
}
