package com.ecommerce.productservice.modules.brand;

import com.ecommerce.productservice.common.api.ApiResponse;
import com.ecommerce.productservice.common.api.PageResponse;
import com.ecommerce.productservice.common.pagination.BrandActivePageParams;
import com.ecommerce.productservice.common.pagination.PageParams;
import com.ecommerce.productservice.modules.brand.dto.BrandResponse;
import com.ecommerce.productservice.modules.brand.dto.CreateBrandRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BrandResponse>> createBrand(
            @Valid @RequestBody CreateBrandRequest request) {
        BrandResponse response = brandService.createBrand(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Brand created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandById(@PathVariable UUID id) {
        BrandResponse response = brandService.getBrandById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandBySlug(@PathVariable String slug) {
        BrandResponse response = brandService.getBrandBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllBrands(
            @ParameterObject PageParams pageParams) {
        PageResponse<BrandResponse> response = brandService.getAllBrands(pageParams);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getActiveBrands(
            @ParameterObject BrandActivePageParams pageParams) {
        PageResponse<BrandResponse> response = brandService.getActiveBrands(pageParams);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(
            @PathVariable UUID id,
            @Valid @RequestBody CreateBrandRequest request) {
        BrandResponse response = brandService.updateBrand(id, request);
        return ResponseEntity.ok(ApiResponse.success("Brand updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(@PathVariable UUID id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok(ApiResponse.success("Brand deleted successfully", null));
    }
}
