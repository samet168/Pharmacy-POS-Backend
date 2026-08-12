package com.pharmacy.pos.branch.service;

import com.pharmacy.pos.common.exception.DuplicateResourceException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.branch.dto.DeviceRequest;
import com.pharmacy.pos.branch.dto.DeviceResponse;
import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.branch.entity.Device;
import com.pharmacy.pos.branch.mapper.DeviceMapper;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.branch.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;
    private final BranchRepository branchRepository;

    @Transactional
    public DeviceResponse create(DeviceRequest request) {
        if (deviceRepository.existsByDeviceUuid(request.getDeviceUuid())) {
            throw new DuplicateResourceException("Device with this UUID already exists");
        }

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch", request.getBranchId()));

        Device device = deviceMapper.toEntity(request);
        device.setBranch(branch);
        device.setRegisteredAt(LocalDateTime.now());
        device = deviceRepository.save(device);
        return deviceMapper.toResponse(device);
    }

    @Transactional
    public DeviceResponse update(Long id, DeviceRequest request) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device", id));

        if (!device.getDeviceUuid().equals(request.getDeviceUuid()) &&
            deviceRepository.existsByDeviceUuid(request.getDeviceUuid())) {
            throw new DuplicateResourceException("Device with this UUID already exists");
        }

        if (!device.getBranch().getId().equals(request.getBranchId())) {
            Branch branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch", request.getBranchId()));
            device.setBranch(branch);
        }

        deviceMapper.updateEntityFromRequest(request, device);
        device = deviceRepository.save(device);
        return deviceMapper.toResponse(device);
    }

    @Transactional
    public DeviceResponse updateLastSynced(String deviceUuid) {
        Device device = deviceRepository.findByDeviceUuid(deviceUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with UUID: " + deviceUuid));
        device.setLastSyncedAt(LocalDateTime.now());
        device = deviceRepository.save(device);
        return deviceMapper.toResponse(device);
    }

    public DeviceResponse getById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device", id));
        return deviceMapper.toResponse(device);
    }

    public DeviceResponse getByDeviceUuid(String deviceUuid) {
        Device device = deviceRepository.findByDeviceUuid(deviceUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with UUID: " + deviceUuid));
        return deviceMapper.toResponse(device);
    }

    public Page<DeviceResponse> getByBranch(Long branchId, Pageable pageable) {
        return deviceRepository.findAllByBranchId(branchId, pageable)
                .map(deviceMapper::toResponse);
    }

    public Page<DeviceResponse> getAll(Pageable pageable) {
        return deviceRepository.findAll(pageable)
                .map(deviceMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device", id));
        deviceRepository.delete(device);
    }
}
