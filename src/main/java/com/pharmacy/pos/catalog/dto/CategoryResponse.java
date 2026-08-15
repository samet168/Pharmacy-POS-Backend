package com.pharmacy.pos.catalog.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Fix: removed hand-written isActive()/setActive() methods that conflict
 * with the ones Lombok @Data generates for the "active" boolean field,
 * causing a compilation error ("duplicate method").
 */
@Data
public class CategoryResponse {
    private Long id;
    private Long organizationId;
    private String name;
    private String nameKh;
    private Long parentId;
    private String parentName;
    private boolean active;    // Lombok generates isActive() / setActive() automatically
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
