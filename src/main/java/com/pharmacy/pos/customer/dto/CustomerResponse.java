package com.pharmacy.pos.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private Long organizationId;
    private String name;
    private String phone;
    private LocalDate dateOfBirth;
    private Integer loyaltyPoints;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
