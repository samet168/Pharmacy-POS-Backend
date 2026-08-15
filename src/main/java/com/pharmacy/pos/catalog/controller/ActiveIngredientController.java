package com.pharmacy.pos.catalog.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.catalog.dto.ActiveIngredientRequest;
import com.pharmacy.pos.catalog.dto.ActiveIngredientResponse;
import com.pharmacy.pos.catalog.service.ActiveIngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/active-ingredients")
@RequiredArgsConstructor
public class ActiveIngredientController {

    private final ActiveIngredientService activeIngredientService;

    @GetMapping
    public ApiResponse<List<ActiveIngredientResponse>> getAll() {
        return ApiResponse.success(activeIngredientService.getAll());
    }

    @GetMapping("/organization/{organizationId}")
    public ApiResponse<List<ActiveIngredientResponse>> getByOrganization(@PathVariable Long organizationId) {
        return ApiResponse.success(activeIngredientService.getByOrganization(organizationId));
    }

    @GetMapping("/{id}")
    public ApiResponse<ActiveIngredientResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(activeIngredientService.getById(id));
    }

    @PostMapping
    public ApiResponse<ActiveIngredientResponse> create(@Valid @RequestBody ActiveIngredientRequest request) {
        return ApiResponse.success(activeIngredientService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<ActiveIngredientResponse> update(@PathVariable Long id, @Valid @RequestBody ActiveIngredientRequest request) {
        return ApiResponse.success(activeIngredientService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        activeIngredientService.delete(id);
        return ApiResponse.success("Active ingredient deleted successfully", null);
    }
}