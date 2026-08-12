package com.pharmacy.pos.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAllergyResponse {
    private Long id;
    private Long customerId;
    private Long ingredientId;
    private String reactionNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
