package com.pharmacy.pos.sales.mapper;

import com.pharmacy.pos.sales.dto.PromotionRequest;
import com.pharmacy.pos.sales.dto.PromotionResponse;
import com.pharmacy.pos.sales.entity.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PromotionMapper {

    public Promotion toEntity(PromotionRequest request) {
        Promotion promotion = new Promotion();
        promotion.setOrganizationId(request.getOrganizationId());
        promotion.setName(request.getName());
        promotion.setCode(request.getCode());
        promotion.setDescription(request.getDescription());
        promotion.setPromotionType(request.getPromotionType());
        promotion.setDiscountValue(request.getDiscountValue());
        promotion.setMinPurchaseAmount(request.getMinPurchaseAmount());
        promotion.setMaxDiscountAmount(request.getMaxDiscountAmount());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setActive(request.getIsActive() != null ? request.getIsActive() : true);
        promotion.setUsageLimit(request.getUsageLimit());
        promotion.setCustomerType(request.getCustomerType());
        return promotion;
    }

    public PromotionResponse toResponse(Promotion promotion) {
        PromotionResponse response = new PromotionResponse();
        response.setId(promotion.getId());
        response.setOrganizationId(promotion.getOrganizationId());
        response.setName(promotion.getName());
        response.setCode(promotion.getCode());
        response.setDescription(promotion.getDescription());
        response.setPromotionType(promotion.getPromotionType());
        response.setDiscountValue(promotion.getDiscountValue());
        response.setMinPurchaseAmount(promotion.getMinPurchaseAmount());
        response.setMaxDiscountAmount(promotion.getMaxDiscountAmount());
        response.setStartDate(promotion.getStartDate());
        response.setEndDate(promotion.getEndDate());
        response.setActive(promotion.isActive());
        response.setUsageLimit(promotion.getUsageLimit());
        response.setUsageCount(promotion.getUsageCount());
        response.setCustomerType(promotion.getCustomerType());
        response.setCreatedAt(promotion.getCreatedAt());
        response.setUpdatedAt(promotion.getUpdatedAt());
        return response;
    }

    public void updateEntityFromRequest(Promotion promotion, PromotionRequest request) {
        promotion.setOrganizationId(request.getOrganizationId());
        promotion.setName(request.getName());
        promotion.setCode(request.getCode());
        promotion.setDescription(request.getDescription());
        promotion.setPromotionType(request.getPromotionType());
        promotion.setDiscountValue(request.getDiscountValue());
        promotion.setMinPurchaseAmount(request.getMinPurchaseAmount());
        promotion.setMaxDiscountAmount(request.getMaxDiscountAmount());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setActive(request.getIsActive() != null ? request.getIsActive() : true);
        promotion.setUsageLimit(request.getUsageLimit());
        promotion.setCustomerType(request.getCustomerType());
    }
}
