package com.pharmacy.pos.iam.service;

import com.pharmacy.pos.common.exception.DuplicateResourceException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.customer.entity.Doctor;
import com.pharmacy.pos.customer.repository.DoctorRepository;
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
import com.pharmacy.pos.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final UserBranchRepository userBranchRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final DoctorRepository doctorRepository;

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

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (request.getPassword().length() < 6) {
                throw new com.pharmacy.pos.common.exception.BusinessRuleException("Password must be at least 6 characters long");
            }
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

        // Auto-create Doctor Profile if user has DOCTOR role
        if ("DOCTOR".equalsIgnoreCase(role.getName()) && !doctorRepository.existsByUserId(user.getId())) {
            Doctor doctor = new Doctor();
            doctor.setUser(user);
            doctor.setName(user.getName());
            doctor.setPhone(user.getPhone());
            doctor.setImageUrl(user.getImageUrl());
            doctor.setSpecialty("General Medicine (ព្យាបាលទូទៅ)");
            doctor.setClinicName("សាខាកណ្តាល (Main Branch)");
            doctor.setDegree("MD - Specialist");
            doctor.setExperienceYears(0);
            doctor.setRating(5.0);
            doctor.setReviewsCount(0);
            doctor.setFee(20.0);
            doctor.setAvailableSlots("09:00 AM, 10:30 AM, 02:00 PM, 04:15 PM");
            doctor.setAvailableDays("Mon, Tue, Wed, Thu, Fri");
            doctorRepository.save(doctor);
            log.info("Auto-created Doctor Profile for user {} ({})", user.getId(), user.getName());
        }

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse createWithImage(UserRequest request, MultipartFile file) throws Exception {
        String imageUrl = cloudinaryService.uploadUserImage(file);
        request.setImageUrl(imageUrl);
        return create(request);
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

        // Only update password if provided
        if (request.getPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        // Only update pin code if provided
        if (request.getPinCode() != null) {
            user.setPinCode(passwordEncoder.encode(request.getPinCode()));
        }

        // Handle branch updates only if branchIds is provided
        if (request.getBranchIds() != null) {
            List<Long> currentBranchIds = userBranchRepository.findBranchIdsByUserId(id);
            
            // Only update if branchIds are different
            if (!currentBranchIds.equals(request.getBranchIds())) {
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
        }

        // Note: imageUrl is handled separately in updateWithImage method
        // We don't use the mapper for imageUrl to preserve existing values
        userMapper.updateEntityFromRequest(request, user);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateWithImage(Long id, UserRequest request, MultipartFile file) throws Exception {
        String imageUrl = cloudinaryService.uploadUserImage(file);
        
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

        // Only update password if provided
        if (request.getPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        // Only update pin code if provided
        if (request.getPinCode() != null) {
            user.setPinCode(passwordEncoder.encode(request.getPinCode()));
        }

        // Handle branch updates only if branchIds is provided
        if (request.getBranchIds() != null) {
            List<Long> currentBranchIds = userBranchRepository.findBranchIdsByUserId(id);
            
            // Only update if branchIds are different
            if (!currentBranchIds.equals(request.getBranchIds())) {
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
        }

        // Manually set imageUrl since mapper ignores it
        user.setImageUrl(imageUrl);
        
        // Use mapper for other fields (but not imageUrl, password, pinCode)
        userMapper.updateEntityFromRequest(request, user);
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

    public Page<UserResponse> getByOrganization(Long organizationId, Pageable pageable) {
        return userRepository.findByOrganizationId(organizationId, pageable)
                .map(userMapper::toResponse);
    }

    @Transactional
    public void resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        
        // Delete user-branch relationships first
        userBranchRepository.deleteByUserId(id);
        
        userRepository.delete(user);
    }
}
