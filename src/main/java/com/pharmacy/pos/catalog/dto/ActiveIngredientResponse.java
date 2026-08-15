package com.pharmacy.pos.catalog.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActiveIngredientResponse {
    private Long id;
    private Long organizationId;
    private String name;
    private String nameKh;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}