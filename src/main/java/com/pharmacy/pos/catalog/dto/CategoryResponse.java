package com.pharmacy.pos.catalog.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Fix: removed hand-written isActive()/setActive() that conflict with the
 * methods Lombok @Data auto-generates for the boolean "active" field —
 * this caused a "duplicate method" compile error.
 */
@Data
public class CategoryResponse {
    private Long id;
    private Long organizationId;
    private String name;
    private String nameKh;
    private Long parentId;
    private String parentName;
    private boolean active;   // Lombok generates isActive() / setActive() correctly
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
