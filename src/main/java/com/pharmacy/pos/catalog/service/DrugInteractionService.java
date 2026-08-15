package com.pharmacy.pos.catalog.service;

import com.pharmacy.pos.catalog.dto.DrugInteractionRequest;
import com.pharmacy.pos.catalog.dto.DrugInteractionResponse;
import com.pharmacy.pos.catalog.entity.DrugInteraction;
import com.pharmacy.pos.catalog.mapper.DrugInteractionMapper;
import com.pharmacy.pos.catalog.repository.DrugInteractionRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrugInteractionService {

    private final DrugInteractionRepository drugInteractionRepository;
    private final DrugInteractionMapper drugInteractionMapper;

    public List<DrugInteractionResponse> getAll() {
        return drugInteractionRepository.findAll().stream()
                .map(drugInteractionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<DrugInteractionResponse> getByIngredient(Long ingredientId) {
        return drugInteractionRepository.findByIngredientAIdOrIngredientBId(ingredientId, ingredientId).stream()
                .map(drugInteractionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public DrugInteractionResponse getById(Long id) {
        DrugInteraction interaction = drugInteractionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drug interaction not found with id: " + id));
        return drugInteractionMapper.toResponse(interaction);
    }

    @Transactional
    public DrugInteractionResponse create(DrugInteractionRequest request) {
        DrugInteraction interaction = drugInteractionMapper.toEntity(request);
        DrugInteraction saved = drugInteractionRepository.save(interaction);
        return drugInteractionMapper.toResponse(saved);
    }

    @Transactional
    public DrugInteractionResponse update(Long id, DrugInteractionRequest request) {
        DrugInteraction interaction = drugInteractionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drug interaction not found with id: " + id));

        drugInteractionMapper.updateEntityFromRequest(interaction, request);
        DrugInteraction updated = drugInteractionRepository.save(interaction);
        return drugInteractionMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        DrugInteraction interaction = drugInteractionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drug interaction not found with id: " + id));
        drugInteractionRepository.delete(interaction);
    }
}
