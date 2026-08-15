package com.pharmacy.pos.catalog.mapper;

import com.pharmacy.pos.catalog.dto.ActiveIngredientRequest;
import com.pharmacy.pos.catalog.dto.ActiveIngredientResponse;
import com.pharmacy.pos.catalog.entity.ActiveIngredient;
import org.springframework.stereotype.Component;

@Component
public class ActiveIngredientMapper {

    public ActiveIngredient toEntity(ActiveIngredientRequest request) {
        ActiveIngredient ingredient = new ActiveIngredient();
        ingredient.setName(request.getName());
        ingredient.setNameKh(request.getNameKh());
        ingredient.setDescription(request.getDescription());
        return ingredient;
    }

    public ActiveIngredientResponse toResponse(ActiveIngredient ingredient) {
        ActiveIngredientResponse response = new ActiveIngredientResponse();
        response.setId(ingredient.getId());
        response.setOrganizationId(ingredient.getOrganization() != null ? ingredient.getOrganization().getId() : null);
        response.setName(ingredient.getName());
        response.setNameKh(ingredient.getNameKh());
        response.setDescription(ingredient.getDescription());
        response.setCreatedAt(ingredient.getCreatedAt());
        response.setUpdatedAt(ingredient.getUpdatedAt());
        return response;
    }

    public void updateEntityFromRequest(ActiveIngredient ingredient, ActiveIngredientRequest request) {
        ingredient.setName(request.getName());
        ingredient.setNameKh(request.getNameKh());
        ingredient.setDescription(request.getDescription());
    }
}