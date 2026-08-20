package com.pharmacy.pos.catalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Fix: Primitive boolean fields named "isXxx" cause Lombok @Data to generate
 * getters "isIsXxx()" (double-prefix). MapStruct resolves "isActive" as a
 * property named "isActive" but the setter is "setIsActive()" — which Lombok
 * does NOT generate for a field named "isActive". Result: always false.
 *
 * Solution: name the fields WITHOUT the "is" prefix. Keep JSON wire names
 * with @JsonProperty so the frontend still receives "isActive" / "isControlledSubstance".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private Long organizationId;
    private String sku;
    private String brandName;
    private Long genericNameId;
    private Long categoryId;
    private Long defaultSupplierId;

    // Boolean (boxed) avoids primitive "isXxx" getter-name collision
    private Boolean requiresPrescription;

    @JsonProperty("isControlledSubstance")
    private Boolean controlledSubstance;   // entity field: isControlledSubstance

    private String imageUrl;
    private Integer minStockAlert;

    @JsonProperty("isActive")
    private Boolean active;                // entity field: active (column: is_active)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
