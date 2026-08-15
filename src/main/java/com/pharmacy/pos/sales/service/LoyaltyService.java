package com.pharmacy.pos.sales.service;

import com.pharmacy.pos.sales.dto.LoyaltyRequest;
import com.pharmacy.pos.sales.dto.LoyaltyResponse;
import com.pharmacy.pos.sales.entity.Loyalty;
import com.pharmacy.pos.sales.mapper.LoyaltyMapper;
import com.pharmacy.pos.sales.repository.LoyaltyRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoyaltyService {

    private final LoyaltyRepository loyaltyRepository;
    private final LoyaltyMapper loyaltyMapper;

    public List<LoyaltyResponse> getAll() {
        return loyaltyRepository.findAll().stream()
                .map(loyaltyMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<LoyaltyResponse> getByOrganization(Long organizationId) {
        return loyaltyRepository.findByOrganizationId(organizationId).stream()
                .map(loyaltyMapper::toResponse)
                .collect(Collectors.toList());
    }

    public LoyaltyResponse getActiveByOrganization(Long organizationId) {
        Loyalty loyalty = loyaltyRepository.findByOrganizationIdAndIsActiveTrue(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Active loyalty program not found for organization: " + organizationId));
        return loyaltyMapper.toResponse(loyalty);
    }

    public LoyaltyResponse getById(Long id) {
        Loyalty loyalty = loyaltyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loyalty program not found with id: " + id));
        return loyaltyMapper.toResponse(loyalty);
    }

    @Transactional
    public LoyaltyResponse create(LoyaltyRequest request) {
        Loyalty loyalty = loyaltyMapper.toEntity(request);
        Loyalty saved = loyaltyRepository.save(loyalty);
        return loyaltyMapper.toResponse(saved);
    }

    @Transactional
    public LoyaltyResponse update(Long id, LoyaltyRequest request) {
        Loyalty loyalty = loyaltyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loyalty program not found with id: " + id));

        loyaltyMapper.updateEntityFromRequest(loyalty, request);
        Loyalty updated = loyaltyRepository.save(loyalty);
        return loyaltyMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        Loyalty loyalty = loyaltyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loyalty program not found with id: " + id));
        loyaltyRepository.delete(loyalty);
    }
}
