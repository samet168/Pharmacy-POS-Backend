package com.pharmacy.pos.iam.service;

import com.pharmacy.pos.common.exception.BusinessRuleException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.iam.dto.LoginRequest;
import com.pharmacy.pos.iam.dto.LoginResponse;
import com.pharmacy.pos.iam.dto.PinLoginRequest;
import com.pharmacy.pos.iam.dto.RefreshTokenRequest;
import com.pharmacy.pos.iam.dto.RegisterRequest;
import com.pharmacy.pos.iam.entity.Role;
import com.pharmacy.pos.iam.entity.User;
import com.pharmacy.pos.iam.entity.UserBranch;
import com.pharmacy.pos.iam.repository.RoleRepository;
import com.pharmacy.pos.iam.repository.UserBranchRepository;
import com.pharmacy.pos.iam.repository.UserRepository;
import com.pharmacy.pos.security.JwtService;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserBranchRepository userBranchRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final OrganizationRepository organizationRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessRuleException("Username already exists");
        }

        // Load organization and role
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));
        
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", request.getRoleId()));

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setPinCode(request.getPinCode());
        user.setOrganization(organization);
        user.setRole(role);
        user.setActive(true);
        
        user = userRepository.save(user);

        // Add branch association if provided
        if (request.getBranchId() != null) {
            // TODO: Load and set Branch entity properly
            // For now, we'll skip this as it requires Branch entity loading
            log.info("TODO: Add branch association for user {} to branch {}", user.getId(), request.getBranchId());
        }

        List<Long> branchIds = userBranchRepository.findBranchIdsByUserId(user.getId());

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getOrganization().getId(),
                user.getRole().getId(),
                branchIds
        );

        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return new LoginResponse(
                accessToken,
                refreshToken,
                user.getId(),
                user.getUsername(),
                user.getOrganization().getId(),
                user.getRole().getId(),
                user.getRole().getName()
        );
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isActive()) {
            throw new BusinessRuleException("User account is inactive");
        }

        List<Long> branchIds = userBranchRepository.findBranchIdsByUserId(user.getId());

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getOrganization().getId(),
                user.getRole().getId(),
                branchIds
        );

        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return new LoginResponse(
                accessToken,
                refreshToken,
                user.getId(),
                user.getUsername(),
                user.getOrganization().getId(),
                user.getRole().getId(),
                user.getRole().getName()
        );
    }

    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        if (!jwtService.isTokenValid(request.getRefreshToken()) ||
            !jwtService.isRefreshToken(request.getRefreshToken())) {
            throw new BusinessRuleException("Invalid refresh token");
        }

        Long userId = jwtService.extractUserId(request.getRefreshToken());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isActive()) {
            throw new BusinessRuleException("User account is inactive");
        }

        List<Long> branchIds = userBranchRepository.findBranchIdsByUserId(user.getId());

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getOrganization().getId(),
                user.getRole().getId(),
                branchIds
        );

        String newRefreshToken = jwtService.generateRefreshToken(user.getId());

        return new LoginResponse(
                accessToken,
                newRefreshToken,
                user.getId(),
                user.getUsername(),
                user.getOrganization().getId(),
                user.getRole().getId(),
                user.getRole().getName()
        );
    }

    @Transactional
    public LoginResponse pinLogin(PinLoginRequest request) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getPinCode() != null && 
                             passwordEncoder.matches(request.getPinCode(), u.getPinCode()))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("Invalid PIN code"));

        if (!user.isActive()) {
            throw new BusinessRuleException("User account is inactive");
        }

        List<Long> branchIds = userBranchRepository.findBranchIdsByUserId(user.getId());
        if (!branchIds.contains(request.getBranchId())) {
            throw new BusinessRuleException("User does not have access to this branch");
        }

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getOrganization().getId(),
                user.getRole().getId(),
                branchIds
        );

        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return new LoginResponse(
                accessToken,
                refreshToken,
                user.getId(),
                user.getUsername(),
                user.getOrganization().getId(),
                user.getRole().getId(),
                user.getRole().getName()
        );
    }
}
