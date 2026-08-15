package com.pharmacy.pos.customer.service;

import com.pharmacy.pos.common.exception.DuplicateResourceException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.customer.dto.CustomerRequest;
import com.pharmacy.pos.customer.dto.CustomerResponse;
import com.pharmacy.pos.customer.entity.Customer;
import com.pharmacy.pos.customer.mapper.CustomerMapper;
import com.pharmacy.pos.customer.repository.CustomerAllergyRepository;
import com.pharmacy.pos.customer.repository.CustomerRepository;
import com.pharmacy.pos.service.CloudinaryService;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final OrganizationRepository organizationRepository;
    private final CustomerAllergyRepository customerAllergyRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        if (request.getPhone() != null &&
            customerRepository.existsByOrganizationIdAndPhone(request.getOrganizationId(), request.getPhone())) {
            throw new DuplicateResourceException("Customer with this phone number already exists for this organization");
        }

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));

        Customer customer = customerMapper.toEntity(request);
        customer.setOrganization(organization);
        if (customer.getLoyaltyPoints() == null) {
            customer.setLoyaltyPoints(0);
        }
        customer = customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse createWithImage(CustomerRequest request, MultipartFile file) throws Exception {
        String imageUrl = cloudinaryService.uploadCustomerImage(file);
        request.setImageUrl(imageUrl);
        return create(request);
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));

        if (request.getPhone() != null && !request.getPhone().equals(customer.getPhone()) &&
            customerRepository.existsByOrganizationIdAndPhone(request.getOrganizationId(), request.getPhone())) {
            throw new DuplicateResourceException("Customer with this phone number already exists for this organization");
        }

        if (!customer.getOrganization().getId().equals(request.getOrganizationId())) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));
            customer.setOrganization(organization);
        }

        // Note: imageUrl is handled separately in updateWithImage method
        // We don't use the mapper for imageUrl to preserve existing values
        customerMapper.updateEntityFromRequest(request, customer);
        customer = customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse updateWithImage(Long id, CustomerRequest request, MultipartFile file) throws Exception {
        String imageUrl = cloudinaryService.uploadCustomerImage(file);
        
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));

        if (request.getPhone() != null && !request.getPhone().equals(customer.getPhone()) &&
            customerRepository.existsByOrganizationIdAndPhone(request.getOrganizationId(), request.getPhone())) {
            throw new DuplicateResourceException("Customer with this phone number already exists for this organization");
        }

        if (!customer.getOrganization().getId().equals(request.getOrganizationId())) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));
            customer.setOrganization(organization);
        }

        // Manually set imageUrl since mapper ignores it
        customer.setImageUrl(imageUrl);
        
        // Use mapper for other fields (but not imageUrl)
        customerMapper.updateEntityFromRequest(request, customer);
        customer = customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }

    public CustomerResponse getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        return customerMapper.toResponse(customer);
    }

    public CustomerResponse getByOrganizationAndPhone(Long organizationId, String phone) {
        return customerRepository.findByOrganizationIdAndPhone(organizationId, phone)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Customer not found with phone: " + phone));
    }

    public Page<CustomerResponse> getByOrganization(Long organizationId, Pageable pageable) {
        return customerRepository.findByOrganizationId(organizationId, pageable)
                .map(customerMapper::toResponse);
    }

    public Page<CustomerResponse> getAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::toResponse);
    }

    public Page<CustomerResponse> search(Long organizationId, String query, Pageable pageable) {
        return customerRepository.findByOrganizationIdAndNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                organizationId, query, query, pageable)
                .map(customerMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        
        // Delete customer allergies first to avoid foreign key constraint
        customerAllergyRepository.deleteByCustomerId(id);
        
        customerRepository.delete(customer);
    }
}
