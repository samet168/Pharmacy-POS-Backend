package com.pharmacy.pos.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerRequest {
    @NotNull
    private Long organizationId;

    @NotBlank
    private String name;

    private String phone;

    private LocalDate dateOfBirth;

    private Integer loyaltyPoints;
}
