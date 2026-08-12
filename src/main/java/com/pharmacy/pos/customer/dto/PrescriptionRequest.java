package com.pharmacy.pos.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PrescriptionRequest {
    @NotNull
    private Long customerId;

    private Long doctorId;

    private String prescriptionImageUrl;

    private LocalDate issuedDate;

    private Boolean refillable;

    private Integer refillsRemaining;

    private List<PrescriptionItemRequest> items;
}
