package com.pharmacy.pos.iam.service;

import com.pharmacy.pos.common.exception.BusinessRuleException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.iam.dto.ShiftRequest;
import com.pharmacy.pos.iam.dto.ShiftResponse;
import com.pharmacy.pos.iam.entity.Shift;
import com.pharmacy.pos.iam.entity.User;
import com.pharmacy.pos.iam.mapper.ShiftMapper;
import com.pharmacy.pos.iam.repository.ShiftRepository;
import com.pharmacy.pos.iam.repository.UserRepository;
import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.branch.entity.Device;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.branch.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final DeviceRepository deviceRepository;

    @Transactional
    public ShiftResponse openShift(ShiftRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch", request.getBranchId()));

        if (shiftRepository.findByUserIdAndStatus(request.getUserId(), com.pharmacy.pos.common.enums.ShiftStatus.OPEN).isPresent()) {
            throw new BusinessRuleException("User already has an open shift");
        }

        Shift shift = shiftMapper.toEntity(request);
        shift.setUser(user);
        shift.setBranch(branch);
        shift.setOpenedAt(LocalDateTime.now());
        shift.setStatus(com.pharmacy.pos.common.enums.ShiftStatus.OPEN);

        if (request.getDeviceId() != null) {
            Device device = deviceRepository.findById(request.getDeviceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Device", request.getDeviceId()));
            shift.setDevice(device);
        }

        shift = shiftRepository.save(shift);
        return shiftMapper.toResponse(shift);
    }

    @Transactional
    public ShiftResponse closeShift(Long id, ShiftRequest request) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift", id));

        if (shift.getStatus() != com.pharmacy.pos.common.enums.ShiftStatus.OPEN) {
            throw new BusinessRuleException("Shift is not open");
        }

        if (request.getActualCash() != null) {
            shift.setActualCash(request.getActualCash());
        }

        if (request.getExpectedCash() != null) {
            shift.setExpectedCash(request.getExpectedCash());
        }

        if (shift.getActualCash() != null && shift.getExpectedCash() != null) {
            shift.setDifference(shift.getActualCash().subtract(shift.getExpectedCash()));
        }

        shift.setStatus(com.pharmacy.pos.common.enums.ShiftStatus.CLOSED);
        shift.setClosedAt(LocalDateTime.now());
        shift = shiftRepository.save(shift);
        return shiftMapper.toResponse(shift);
    }

    public ShiftResponse getById(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift", id));
        return shiftMapper.toResponse(shift);
    }

    public Page<ShiftResponse> getByBranch(Long branchId, Pageable pageable) {
        return shiftRepository.findByBranchIdOrderByOpenedAtDesc(branchId, pageable)
                .map(shiftMapper::toResponse);
    }

    public Page<ShiftResponse> getAll(Pageable pageable) {
        return shiftRepository.findAll(pageable)
                .map(shiftMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift", id));
        shiftRepository.delete(shift);
    }
}
