package com.pharmacy.pos.tenant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationResponse {
    private Long id;
    private String name;
    private String slug;
    private String licenseNumber;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private String logoUrl;
    private String baseCurrency;

    @JsonProperty("isActive")
    private Boolean active;    // renamed from "isActive" to fix Lombok double-prefix getter bug

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
