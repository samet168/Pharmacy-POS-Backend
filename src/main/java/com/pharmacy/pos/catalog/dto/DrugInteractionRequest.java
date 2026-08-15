package com.pharmacy.pos.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DrugInteractionRequest {
    @NotNull
    private Long ingredientAId;

    @NotNull
    private Long ingredientBId;

    @NotBlank
    private String severity;

    private String description;
}
