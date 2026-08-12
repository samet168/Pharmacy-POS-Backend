package com.pharmacy.pos.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponse {
    private Long id;
    private Long customerId;
    private Long doctorId;
    private String prescriptionImageUrl;
    private LocalDate issuedDate;
    private boolean refillable;
    private Integer refillsRemaining;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PrescriptionItemResponse> items;
}
