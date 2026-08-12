package com.pharmacy.pos.iam.service;

import com.pharmacy.pos.common.exception.DuplicateResourceException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.iam.dto.UserRequest;
import com.pharmacy.pos.iam.dto.UserResponse;
import com.pharmacy.pos.iam.entity.Role;
import com.pharmacy.pos.iam.entity.User;
import com.pharmacy.pos.iam.entity.UserBranch;
import com.pharmacy.pos.iam.mapper.UserMapper;
import com.pharmacy.pos.iam.repository.RoleRepository;
import com.pharmacy.pos.iam.repository.UserBranchRepository;
import com.pharmacy.pos.iam.repository.UserRepository;
import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final UserBranchRepository userBranchRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("User with this username already exists");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", request.getRoleId()));

        Organization organization = role.getOrganization();
        if (organization == null) {
            organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));
        }

        User user = userMapper.toEntity(request);
        user.setRole(role);
        user.setOrganization(organization);

        if (request.getPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getPinCode() != null) {
            user.setPinCode(passwordEncoder.encode(request.getPinCode()));
        }

        user = userRepository.save(user);

        if (request.getBranchIds() != null && !request.getBranchIds().isEmpty()) {
            for (Long branchId : request.getBranchIds()) {
                Branch branch = branchRepository.findById(branchId)
                        .orElseThrow(() -> new ResourceNotFoundException("Branch", branchId));
                UserBranch userBranch = new UserBranch();
                userBranch.setUser(user);
                userBranch.setBranch(branch);
                userBranchRepository.save(userBranch);
            }
        }

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        if (!user.getUsername().equals(request.getUsername()) &&
            userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("User with this username already exists");
        }

        if (!user.getRole().getId().equals(request.getRoleId())) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role", request.getRoleId()));
            user.setRole(role);
        }

        userMapper.updateEntityFromRequest(request, user);

        if (request.getPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getPinCode() != null) {
            user.setPinCode(passwordEncoder.encode(request.getPinCode()));
        }

        if (request.getBranchIds() != null) {
            userBranchRepository.deleteByUserId(id);
            for (Long branchId : request.getBranchIds()) {
                Branch branch = branchRepository.findById(branchId)
                        .orElseThrow(() -> new ResourceNotFoundException("Branch", branchId));
                UserBranch userBranch = new UserBranch();
                userBranch.setUser(user);
                userBranch.setBranch(branch);
                userBranchRepository.save(userBranch);
            }
        }

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userMapper.toResponse(user);
    }

    public Page<UserResponse> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        userRepository.delete(user);
    }
}
