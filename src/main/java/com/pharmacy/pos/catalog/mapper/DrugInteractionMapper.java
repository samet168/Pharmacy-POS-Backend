package com.pharmacy.pos.catalog.mapper;

import com.pharmacy.pos.catalog.dto.DrugInteractionRequest;
import com.pharmacy.pos.catalog.dto.DrugInteractionResponse;
import com.pharmacy.pos.catalog.entity.DrugInteraction;
import com.pharmacy.pos.catalog.entity.ActiveIngredient;
import com.pharmacy.pos.catalog.repository.ActiveIngredientRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DrugInteractionMapper {

    private final ActiveIngredientRepository activeIngredientRepository;

    public DrugInteraction toEntity(DrugInteractionRequest request) {
        DrugInteraction interaction = new DrugInteraction();

        ActiveIngredient ingredientA = activeIngredientRepository.findById(request.getIngredientAId())
                .orElseThrow(() -> new ResourceNotFoundException("Active ingredient not found with id: " + request.getIngredientAId()));
        interaction.setIngredientA(ingredientA);

        ActiveIngredient ingredientB = activeIngredientRepository.findById(request.getIngredientBId())
                .orElseThrow(() -> new ResourceNotFoundException("Active ingredient not found with id: " + request.getIngredientBId()));
        interaction.setIngredientB(ingredientB);

        interaction.setSeverity(request.getSeverity());
        interaction.setDescription(request.getDescription());
        return interaction;
    }

    public DrugInteractionResponse toResponse(DrugInteraction interaction) {
        DrugInteractionResponse response = new DrugInteractionResponse();
        response.setId(interaction.getId());
        response.setIngredientAId(interaction.getIngredientA() != null ? interaction.getIngredientA().getId() : null);
        response.setIngredientAName(interaction.getIngredientA() != null ? interaction.getIngredientA().getName() : null);
        response.setIngredientBId(interaction.getIngredientB() != null ? interaction.getIngredientB().getId() : null);
        response.setIngredientBName(interaction.getIngredientB() != null ? interaction.getIngredientB().getName() : null);
        response.setSeverity(interaction.getSeverity());
        response.setDescription(interaction.getDescription());
        response.setCreatedAt(interaction.getCreatedAt());
        return response;
    }

    public void updateEntityFromRequest(DrugInteraction interaction, DrugInteractionRequest request) {
        if (request.getIngredientAId() != null) {
            ActiveIngredient ingredientA = activeIngredientRepository.findById(request.getIngredientAId())
                    .orElseThrow(() -> new ResourceNotFoundException("Active ingredient not found with id: " + request.getIngredientAId()));
            interaction.setIngredientA(ingredientA);
        }

        if (request.getIngredientBId() != null) {
            ActiveIngredient ingredientB = activeIngredientRepository.findById(request.getIngredientBId())
                    .orElseThrow(() -> new ResourceNotFoundException("Active ingredient not found with id: " + request.getIngredientBId()));
            interaction.setIngredientB(ingredientB);
        }

        interaction.setSeverity(request.getSeverity());
        interaction.setDescription(request.getDescription());
    }
}
