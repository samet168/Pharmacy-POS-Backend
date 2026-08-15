package com.pharmacy.pos.sales.service;

import com.pharmacy.pos.sales.dto.PromotionRequest;
import com.pharmacy.pos.sales.dto.PromotionResponse;
import com.pharmacy.pos.sales.entity.Promotion;
import com.pharmacy.pos.sales.mapper.PromotionMapper;
import com.pharmacy.pos.sales.repository.PromotionRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public List<PromotionResponse> getAll() {
        return promotionRepository.findAll().stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PromotionResponse> getByOrganization(Long organizationId) {
        return promotionRepository.findByOrganizationId(organizationId).stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PromotionResponse> getActiveByOrganization(Long organizationId) {
        return promotionRepository.findByOrganizationIdAndIsActiveTrue(organizationId).stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PromotionResponse> getActiveByOrganizationAndDate(Long organizationId, LocalDate date) {
        return promotionRepository.findByOrganizationIdAndIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                organizationId, date, date).stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public PromotionResponse getByCode(String code) {
        Promotion promotion = promotionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with code: " + code));
        return promotionMapper.toResponse(promotion);
    }

    public PromotionResponse getById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with id: " + id));
        return promotionMapper.toResponse(promotion);
    }

    @Transactional
    public PromotionResponse create(PromotionRequest request) {
        if (request.getCode() != null && promotionRepository.findByCode(request.getCode()).isPresent()) {
            throw new IllegalStateException("Promotion code already exists: " + request.getCode());
        }

        Promotion promotion = promotionMapper.toEntity(request);
        Promotion saved = promotionRepository.save(promotion);
        return promotionMapper.toResponse(saved);
    }

    @Transactional
    public PromotionResponse update(Long id, PromotionRequest request) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with id: " + id));

        if (request.getCode() != null && !request.getCode().equals(promotion.getCode())) {
            if (promotionRepository.findByCode(request.getCode()).isPresent()) {
                throw new IllegalStateException("Promotion code already exists: " + request.getCode());
            }
        }

        promotionMapper.updateEntityFromRequest(promotion, request);
        Promotion updated = promotionRepository.save(promotion);
        return promotionMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with id: " + id));
        promotionRepository.delete(promotion);
    }
}
