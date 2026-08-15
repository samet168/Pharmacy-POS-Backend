package com.pharmacy.pos.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrugInteractionResponse {
    private Long id;
    private Long ingredientAId;
    private String ingredientAName;
    private Long ingredientBId;
    private String ingredientBName;
    private String severity;
    private String description;
    private LocalDateTime createdAt;
}
