package com.pharmacy.pos.catalog.service;

import com.pharmacy.pos.catalog.dto.ActiveIngredientRequest;
import com.pharmacy.pos.catalog.dto.ActiveIngredientResponse;
import com.pharmacy.pos.catalog.entity.ActiveIngredient;
import com.pharmacy.pos.catalog.mapper.ActiveIngredientMapper;
import com.pharmacy.pos.catalog.repository.ActiveIngredientRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActiveIngredientService {

    private final ActiveIngredientRepository activeIngredientRepository;
    private final ActiveIngredientMapper activeIngredientMapper;
    private final OrganizationRepository organizationRepository;

    public List<ActiveIngredientResponse> getAll() {
        return activeIngredientRepository.findAll().stream()
                .map(activeIngredientMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ActiveIngredientResponse> getByOrganization(Long organizationId) {
        return activeIngredientRepository.findByOrganizationId(organizationId).stream()
                .map(activeIngredientMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ActiveIngredientResponse getById(Long id) {
        ActiveIngredient ingredient = activeIngredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active ingredient not found with id: " + id));
        return activeIngredientMapper.toResponse(ingredient);
    }

    @Transactional
    public ActiveIngredientResponse create(ActiveIngredientRequest request) {
        ActiveIngredient ingredient = activeIngredientMapper.toEntity(request);
        
        if (request.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrganizationId()));
            ingredient.setOrganization(organization);
        }
        
        ActiveIngredient saved = activeIngredientRepository.save(ingredient);
        return activeIngredientMapper.toResponse(saved);
    }

    @Transactional
    public ActiveIngredientResponse update(Long id, ActiveIngredientRequest request) {
        ActiveIngredient ingredient = activeIngredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active ingredient not found with id: " + id));

        if (request.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrganizationId()));
            ingredient.setOrganization(organization);
        }

        activeIngredientMapper.updateEntityFromRequest(ingredient, request);
        ActiveIngredient updated = activeIngredientRepository.save(ingredient);
        return activeIngredientMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        ActiveIngredient ingredient = activeIngredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active ingredient not found with id: " + id));

        if (!ingredient.getInteractionsAsIngredientA().isEmpty() || !ingredient.getInteractionsAsIngredientB().isEmpty()) {
            throw new IllegalStateException("Cannot delete active ingredient with existing drug interactions");
        }

        activeIngredientRepository.delete(ingredient);
    }
}