package com.pharmacy.pos.customer.service;

import com.pharmacy.pos.catalog.entity.ActiveIngredient;
import com.pharmacy.pos.catalog.repository.ActiveIngredientRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.customer.dto.CustomerAllergyRequest;
import com.pharmacy.pos.customer.dto.CustomerAllergyResponse;
import com.pharmacy.pos.customer.entity.Customer;
import com.pharmacy.pos.customer.entity.CustomerAllergy;
import com.pharmacy.pos.customer.mapper.CustomerAllergyMapper;
import com.pharmacy.pos.customer.repository.CustomerAllergyRepository;
import com.pharmacy.pos.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerAllergyService {

    private final CustomerAllergyRepository customerAllergyRepository;
    private final CustomerAllergyMapper customerAllergyMapper;
    private final CustomerRepository customerRepository;
    private final ActiveIngredientRepository activeIngredientRepository;

    @Transactional
    public CustomerAllergyResponse create(CustomerAllergyRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", request.getCustomerId()));

        ActiveIngredient ingredient = activeIngredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new ResourceNotFoundException("Active Ingredient", request.getIngredientId()));

        CustomerAllergy allergy = customerAllergyMapper.toEntity(request);
        allergy.setCustomer(customer);
        allergy.setIngredient(ingredient);
        allergy = customerAllergyRepository.save(allergy);
        return customerAllergyMapper.toResponse(allergy);
    }

    @Transactional
    public CustomerAllergyResponse update(Long id, CustomerAllergyRequest request) {
        CustomerAllergy allergy = customerAllergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Allergy", id));

        if (!allergy.getCustomer().getId().equals(request.getCustomerId())) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", request.getCustomerId()));
            allergy.setCustomer(customer);
        }

        if (!allergy.getIngredient().getId().equals(request.getIngredientId())) {
            ActiveIngredient ingredient = activeIngredientRepository.findById(request.getIngredientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Active Ingredient", request.getIngredientId()));
            allergy.setIngredient(ingredient);
        }

        customerAllergyMapper.updateEntityFromRequest(request, allergy);
        allergy = customerAllergyRepository.save(allergy);
        return customerAllergyMapper.toResponse(allergy);
    }

    public CustomerAllergyResponse getById(Long id) {
        CustomerAllergy allergy = customerAllergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Allergy", id));
        return customerAllergyMapper.toResponse(allergy);
    }

    public List<CustomerAllergyResponse> getByCustomer(Long customerId) {
        return customerAllergyRepository.findByCustomerId(customerId).stream()
                .map(customerAllergyMapper::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        CustomerAllergy allergy = customerAllergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Allergy", id));
        customerAllergyRepository.delete(allergy);
    }
}
