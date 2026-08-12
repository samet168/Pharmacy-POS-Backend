package com.pharmacy.pos.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerAllergyRequest {
    @NotNull
    private Long customerId;

    @NotNull
    private Long ingredientId;

    private String reactionNotes;
}
