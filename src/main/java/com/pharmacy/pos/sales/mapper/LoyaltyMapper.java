package com.pharmacy.pos.sales.mapper;

import com.pharmacy.pos.sales.dto.LoyaltyRequest;
import com.pharmacy.pos.sales.dto.LoyaltyResponse;
import com.pharmacy.pos.sales.entity.Loyalty;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyMapper {

    public Loyalty toEntity(LoyaltyRequest request) {
        Loyalty loyalty = new Loyalty();
        loyalty.setOrganizationId(request.getOrganizationId());
        loyalty.setName(request.getName());
        loyalty.setDescription(request.getDescription());
        loyalty.setPointsPerDollar(request.getPointsPerDollar());
        loyalty.setRedemptionRate(request.getRedemptionRate());
        loyalty.setMinPointsToRedeem(request.getMinPointsToRedeem());
        loyalty.setMaxPointsPerTransaction(request.getMaxPointsPerTransaction());
        loyalty.setPointsExpiryMonths(request.getPointsExpiryMonths());
        loyalty.setActive(request.getIsActive() != null ? request.getIsActive() : true);
        loyalty.setTierBased(request.getTierBased() != null ? request.getTierBased() : false);
        loyalty.setSilverThreshold(request.getSilverThreshold());
        loyalty.setGoldThreshold(request.getGoldThreshold());
        loyalty.setPlatinumThreshold(request.getPlatinumThreshold());
        loyalty.setSilverMultiplier(request.getSilverMultiplier());
        loyalty.setGoldMultiplier(request.getGoldMultiplier());
        loyalty.setPlatinumMultiplier(request.getPlatinumMultiplier());
        return loyalty;
    }

    public LoyaltyResponse toResponse(Loyalty loyalty) {
        LoyaltyResponse response = new LoyaltyResponse();
        response.setId(loyalty.getId());
        response.setOrganizationId(loyalty.getOrganizationId());
        response.setName(loyalty.getName());
        response.setDescription(loyalty.getDescription());
        response.setPointsPerDollar(loyalty.getPointsPerDollar());
        response.setRedemptionRate(loyalty.getRedemptionRate());
        response.setMinPointsToRedeem(loyalty.getMinPointsToRedeem());
        response.setMaxPointsPerTransaction(loyalty.getMaxPointsPerTransaction());
        response.setPointsExpiryMonths(loyalty.getPointsExpiryMonths());
        response.setActive(loyalty.isActive());
        response.setTierBased(loyalty.isTierBased());
        response.setSilverThreshold(loyalty.getSilverThreshold());
        response.setGoldThreshold(loyalty.getGoldThreshold());
        response.setPlatinumThreshold(loyalty.getPlatinumThreshold());
        response.setSilverMultiplier(loyalty.getSilverMultiplier());
        response.setGoldMultiplier(loyalty.getGoldMultiplier());
        response.setPlatinumMultiplier(loyalty.getPlatinumMultiplier());
        response.setCreatedAt(loyalty.getCreatedAt());
        response.setUpdatedAt(loyalty.getUpdatedAt());
        return response;
    }

    public void updateEntityFromRequest(Loyalty loyalty, LoyaltyRequest request) {
        loyalty.setOrganizationId(request.getOrganizationId());
        loyalty.setName(request.getName());
        loyalty.setDescription(request.getDescription());
        loyalty.setPointsPerDollar(request.getPointsPerDollar());
        loyalty.setRedemptionRate(request.getRedemptionRate());
        loyalty.setMinPointsToRedeem(request.getMinPointsToRedeem());
        loyalty.setMaxPointsPerTransaction(request.getMaxPointsPerTransaction());
        loyalty.setPointsExpiryMonths(request.getPointsExpiryMonths());
        loyalty.setActive(request.getIsActive() != null ? request.getIsActive() : true);
        loyalty.setTierBased(request.getTierBased() != null ? request.getTierBased() : false);
        loyalty.setSilverThreshold(request.getSilverThreshold());
        loyalty.setGoldThreshold(request.getGoldThreshold());
        loyalty.setPlatinumThreshold(request.getPlatinumThreshold());
        loyalty.setSilverMultiplier(request.getSilverMultiplier());
        loyalty.setGoldMultiplier(request.getGoldMultiplier());
        loyalty.setPlatinumMultiplier(request.getPlatinumMultiplier());
    }
}
