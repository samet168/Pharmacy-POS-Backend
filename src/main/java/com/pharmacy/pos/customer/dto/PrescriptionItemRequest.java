package com.pharmacy.pos.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrescriptionItemRequest {
    @NotNull
    private Long productId;

    private String dosageInstruction;

    private Integer quantityPrescribed;
}
