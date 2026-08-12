package com.pharmacy.pos.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItemResponse {
    private Long id;
    private Long prescriptionId;
    private Long productId;
    private String dosageInstruction;
    private Integer quantityPrescribed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
