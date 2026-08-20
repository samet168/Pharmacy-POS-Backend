package com.pharmacy.pos.customer.service;

import com.pharmacy.pos.catalog.entity.ActiveIngredient;
import com.pharmacy.pos.catalog.entity.Product;
import com.pharmacy.pos.catalog.repository.ActiveIngredientRepository;
import com.pharmacy.pos.catalog.repository.ProductRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.customer.dto.PrescriptionItemRequest;
import com.pharmacy.pos.customer.dto.PrescriptionRequest;
import com.pharmacy.pos.customer.dto.PrescriptionResponse;
import com.pharmacy.pos.customer.entity.Customer;
import com.pharmacy.pos.customer.entity.Doctor;
import com.pharmacy.pos.customer.entity.Prescription;
import com.pharmacy.pos.customer.entity.PrescriptionItem;
import com.pharmacy.pos.customer.mapper.PrescriptionItemMapper;
import com.pharmacy.pos.customer.mapper.PrescriptionMapper;
import com.pharmacy.pos.customer.repository.CustomerRepository;
import com.pharmacy.pos.customer.repository.DoctorRepository;
import com.pharmacy.pos.customer.repository.PrescriptionItemRepository;
import com.pharmacy.pos.customer.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final PrescriptionItemMapper prescriptionItemMapper;
    private final CustomerRepository customerRepository;
    private final DoctorRepository doctorRepository;
    private final ProductRepository productRepository;
    private final ActiveIngredientRepository activeIngredientRepository;

    @Transactional
    public PrescriptionResponse create(PrescriptionRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", request.getCustomerId()));

        Doctor doctor = null;
        if (request.getDoctorId() != null) {
            doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor", request.getDoctorId()));
        }

        Prescription prescription = prescriptionMapper.toEntity(request);
        prescription.setCustomer(customer);
        prescription.setDoctor(doctor);

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            Set<PrescriptionItem> items = new HashSet<>();

            for (PrescriptionItemRequest itemRequest : request.getItems()) {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product", itemRequest.getProductId()));

                PrescriptionItem item = prescriptionItemMapper.toEntity(itemRequest);
                item.setPrescription(prescription);
                item.setProduct(product);
                items.add(item);
            }

            prescription.setItems(items);
        }

        prescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toResponse(prescription);
    }

    @Transactional
    public PrescriptionResponse update(Long id, PrescriptionRequest request) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", id));

        if (!prescription.getCustomer().getId().equals(request.getCustomerId())) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", request.getCustomerId()));
            prescription.setCustomer(customer);
        }

        if (request.getDoctorId() != null) {
            if (prescription.getDoctor() == null || !prescription.getDoctor().getId().equals(request.getDoctorId())) {
                Doctor doctor = doctorRepository.findById(request.getDoctorId())
                        .orElseThrow(() -> new ResourceNotFoundException("Doctor", request.getDoctorId()));
                prescription.setDoctor(doctor);
            }
        } else {
            prescription.setDoctor(null);
        }

        prescriptionMapper.updateEntityFromRequest(request, prescription);

        if (request.getItems() != null) {
            prescription.getItems().clear();

            for (PrescriptionItemRequest itemRequest : request.getItems()) {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product", itemRequest.getProductId()));

                PrescriptionItem item = prescriptionItemMapper.toEntity(itemRequest);
                item.setPrescription(prescription);
                item.setProduct(product);
                prescription.getItems().add(item);
            }
        }

        prescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toResponse(prescription);
    }

    public PrescriptionResponse getById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", id));
        return prescriptionMapper.toResponse(prescription);
    }

    public Page<PrescriptionResponse> getByCustomer(Long customerId, Pageable pageable) {
        return prescriptionRepository.findByCustomerId(customerId, pageable)
                .map(prescriptionMapper::toResponse);
    }

    public Page<PrescriptionResponse> getByDoctor(Long doctorId, Pageable pageable) {
        return prescriptionRepository.findByDoctorId(doctorId, pageable)
                .map(prescriptionMapper::toResponse);
    }

    public Page<PrescriptionResponse> getAll(Pageable pageable) {
        return prescriptionRepository.findAll(pageable)
                .map(prescriptionMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", id));
        prescriptionRepository.delete(prescription);
    }

    /**
     * Check if any of the given product IDs have ingredients that the customer is allergic to.
     * Returns a list of allergy matches with product IDs and ingredient names.
     */
    public List<AllergyMatch> checkProductAllergies(Long customerId, List<Long> productIds) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", customerId));

        // Get customer's allergies
        List<Long> allergyIngredientIds = customer.getAllergies().stream()
                .map(allergy -> allergy.getIngredient().getId())
                .toList();

        if (allergyIngredientIds.isEmpty()) {
            return List.of();
        }

        // Get products with their generic names (active ingredients)
        List<Product> products = productRepository.findAllById(productIds);

        return products.stream()
                .filter(product -> product.getGenericNameId() != null &&
                        allergyIngredientIds.contains(product.getGenericNameId()))
                .map(product -> {
                    // TODO: Implement proper ingredient name lookup
                    return new AllergyMatch(
                            product.getId(),
                            product.getSku(),
                            product.getBrandName(),
                            "Unknown ingredient"
                    );
                })
                .toList();
    }

    public record AllergyMatch(
            Long productId,
            String productSku,
            String productName,
            String ingredientName
    ) {}
}
