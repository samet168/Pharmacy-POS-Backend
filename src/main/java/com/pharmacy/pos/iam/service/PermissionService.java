package com.pharmacy.pos.iam.service;

import com.pharmacy.pos.common.exception.DuplicateResourceException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.iam.dto.PermissionRequest;
import com.pharmacy.pos.iam.dto.PermissionResponse;
import com.pharmacy.pos.iam.entity.Permission;
import com.pharmacy.pos.iam.mapper.PermissionMapper;
import com.pharmacy.pos.iam.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Transactional
    public PermissionResponse create(PermissionRequest request) {
        if (permissionRepository.findByCode(request.getCode()).isPresent()) {
            throw new DuplicateResourceException("Permission with this code already exists");
        }

        Permission permission = permissionMapper.toEntity(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toResponse(permission);
    }

    @Transactional
    public PermissionResponse update(Long id, PermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", id));

        if (!permission.getCode().equals(request.getCode()) &&
            permissionRepository.findByCode(request.getCode()).isPresent()) {
            throw new DuplicateResourceException("Permission with this code already exists");
        }

        permissionMapper.updateEntityFromRequest(request, permission);
        permission = permissionRepository.save(permission);
        return permissionMapper.toResponse(permission);
    }

    public PermissionResponse getById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", id));
        return permissionMapper.toResponse(permission);
    }

    public PermissionResponse getByCode(String code) {
        Permission permission = permissionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with code: " + code));
        return permissionMapper.toResponse(permission);
    }

    public Page<PermissionResponse> getAll(Pageable pageable) {
        return permissionRepository.findAll(pageable)
                .map(permissionMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", id));
        permissionRepository.delete(permission);
    }
}
