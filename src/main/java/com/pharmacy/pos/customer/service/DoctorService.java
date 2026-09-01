package com.pharmacy.pos.customer.service;

import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.customer.dto.DoctorRequest;
import com.pharmacy.pos.customer.dto.DoctorResponse;
import com.pharmacy.pos.customer.entity.Doctor;
import com.pharmacy.pos.customer.mapper.DoctorMapper;
import com.pharmacy.pos.customer.repository.DoctorRepository;
import com.pharmacy.pos.customer.repository.PrescriptionRepository;
import com.pharmacy.pos.iam.entity.Role;
import com.pharmacy.pos.iam.entity.User;
import com.pharmacy.pos.iam.entity.UserBranch;
import com.pharmacy.pos.iam.repository.RoleRepository;
import com.pharmacy.pos.iam.repository.UserBranchRepository;
import com.pharmacy.pos.iam.repository.UserRepository;
import com.pharmacy.pos.service.CloudinaryService;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final CloudinaryService cloudinaryService;
    private final PrescriptionRepository prescriptionRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final BranchRepository branchRepository;
    private final UserBranchRepository userBranchRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public DoctorResponse create(DoctorRequest request) {
        Doctor doctor = doctorMapper.toEntity(request);

        // If username is provided, create/link IAM user account
        if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
            String username = request.getUsername().trim();
            User user = userRepository.findByUsername(username).orElse(null);

            if (user == null) {
                user = new User();
                user.setUsername(username);
                user.setName(request.getName());
                user.setPhone(request.getPhone());
                user.setActive(true);

                String rawPassword = (request.getPassword() != null && !request.getPassword().trim().isEmpty())
                        ? request.getPassword().trim()
                        : "123456";
                user.setPasswordHash(passwordEncoder.encode(rawPassword));

                Role doctorRole = roleRepository.findByNameIgnoreCase("DOCTOR")
                        .or(() -> roleRepository.findByNameIgnoreCase("DOCTOR_ROLE"))
                        .orElseGet(() -> roleRepository.findAll().stream().findFirst().orElse(null));
                user.setRole(doctorRole);

                Organization org = organizationRepository.findAll().stream().findFirst().orElse(null);
                user.setOrganization(org);

                user = userRepository.save(user);

                List<Branch> branches = branchRepository.findAll();
                for (Branch b : branches) {
                    UserBranch ub = new UserBranch();
                    ub.setUser(user);
                    ub.setBranch(b);
                    userBranchRepository.save(ub);
                }
            } else if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
                user.setPasswordHash(passwordEncoder.encode(request.getPassword().trim()));
                userRepository.save(user);
            }

            doctor.setUser(user);
        }

        doctor = doctorRepository.save(doctor);
        return doctorMapper.toResponse(doctor);
    }

    @Transactional
    public DoctorResponse createWithImage(DoctorRequest request, MultipartFile file) throws Exception {
        String imageUrl = cloudinaryService.uploadDoctorImage(file);
        request.setImageUrl(imageUrl);
        return create(request);
    }

    @Transactional
    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));

        // Note: imageUrl is handled separately in updateWithImage method
        // We don't use the mapper for imageUrl to preserve existing values
        doctorMapper.updateEntityFromRequest(request, doctor);
        doctor = doctorRepository.save(doctor);
        return doctorMapper.toResponse(doctor);
    }

    @Transactional
    public DoctorResponse updateWithImage(Long id, DoctorRequest request, MultipartFile file) throws Exception {
        String imageUrl = cloudinaryService.uploadDoctorImage(file);
        
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));

        // Manually set imageUrl since mapper ignores it
        doctor.setImageUrl(imageUrl);
        
        // Use mapper for other fields (but not imageUrl)
        doctorMapper.updateEntityFromRequest(request, doctor);
        doctor = doctorRepository.save(doctor);
        return doctorMapper.toResponse(doctor);
    }

    public DoctorResponse getById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));
        return doctorMapper.toResponse(doctor);
    }

    public Page<DoctorResponse> searchByName(String name, Pageable pageable) {
        return doctorRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(doctorMapper::toResponse);
    }

    public Page<DoctorResponse> getAll(Pageable pageable) {
        return doctorRepository.findAll(pageable)
                .map(doctorMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));
        
        // Delete prescriptions first to avoid foreign key constraint
        prescriptionRepository.deleteByDoctorId(id);
        
        doctorRepository.delete(doctor);
    }
}
