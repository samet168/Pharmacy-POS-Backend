package com.pharmacy.pos.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "Category name is required")
    private String name;

    private String nameKh;

    private Long parentId;

    private boolean active = true;

    public boolean isActive() {
        return active;
    }
}