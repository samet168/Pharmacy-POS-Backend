package com.pharmacy.pos.branch.service;

import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.branch.dto.BranchSettingsRequest;
import com.pharmacy.pos.branch.dto.BranchSettingsResponse;
import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.branch.entity.BranchSettings;
import com.pharmacy.pos.branch.mapper.BranchSettingsMapper;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.branch.repository.BranchSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BranchSettingsService {

    private final BranchSettingsRepository branchSettingsRepository;
    private final BranchSettingsMapper branchSettingsMapper;
    private final BranchRepository branchRepository;

    @Transactional
    public BranchSettingsResponse createOrUpdate(BranchSettingsRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch", request.getBranchId()));

        BranchSettings settings = branchSettingsRepository.findByBranchId(request.getBranchId())
                .orElse(new BranchSettings());

        branchSettingsMapper.updateEntityFromRequest(request, settings);
        settings.setBranch(branch);
        settings.setUpdatedAt(LocalDateTime.now());
        settings = branchSettingsRepository.save(settings);
        return branchSettingsMapper.toResponse(settings);
    }

    public BranchSettingsResponse getByBranchId(Long branchId) {
        BranchSettings settings = branchSettingsRepository.findByBranchId(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch settings not found for branch: " + branchId));
        return branchSettingsMapper.toResponse(settings);
    }
}
