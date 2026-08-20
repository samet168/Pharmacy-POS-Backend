package com.pharmacy.pos.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActiveIngredientRequest {
    @NotBlank(message = "Active ingredient name is required")
    private String name;

    private String nameKh;

    private String description;

    private Long organizationId;
}