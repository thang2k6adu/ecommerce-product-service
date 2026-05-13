package com.ecommerce.productservice.modules.brand;

import com.ecommerce.productservice.common.exception.BadRequestException;
import com.ecommerce.productservice.common.exception.ResourceNotFoundException;
import com.ecommerce.productservice.modules.brand.dto.BrandResponse;
import com.ecommerce.productservice.modules.brand.dto.CreateBrandRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public BrandResponse createBrand(CreateBrandRequest request) {
        log.info("Creating brand: {}", request.getName());

        if (request.getSlug() != null && brandRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Brand slug already exists: " + request.getSlug());
        }

        BrandEntity brand = brandMapper.toEntity(request);
        BrandEntity savedBrand = brandRepository.save(brand);

        log.info("Brand created successfully with ID: {}", savedBrand.getId());
        return brandMapper.toResponse(savedBrand);
    }

    @Transactional(readOnly = true)
    public BrandResponse getBrandById(UUID id) {
        BrandEntity brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));
        return brandMapper.toResponse(brand);
    }

    @Transactional(readOnly = true)
    public BrandResponse getBrandBySlug(String slug) {
        BrandEntity brand = brandRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "slug", slug));
        return brandMapper.toResponse(brand);
    }

    @Transactional(readOnly = true)
    public List<BrandResponse> getAllBrands() {
        List<BrandEntity> brands = brandRepository.findAll();
        return brandMapper.toResponseList(brands);
    }

    @Transactional(readOnly = true)
    public List<BrandResponse> getActiveBrands() {
        List<BrandEntity> brands = brandRepository.findByActiveTrue();
        return brandMapper.toResponseList(brands);
    }

    public BrandResponse updateBrand(UUID id, CreateBrandRequest request) {
        log.info("Updating brand: {}", id);

        BrandEntity brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));

        if (request.getSlug() != null && !request.getSlug().equals(brand.getSlug())) {
            if (brandRepository.existsBySlug(request.getSlug())) {
                throw new BadRequestException("Brand slug already exists: " + request.getSlug());
            }
        }

        brandMapper.updateEntityFromRequest(request, brand);
        BrandEntity updatedBrand = brandRepository.save(brand);

        log.info("Brand updated successfully: {}", id);
        return brandMapper.toResponse(updatedBrand);
    }

    public void deleteBrand(UUID id) {
        log.info("Deleting brand: {}", id);

        BrandEntity brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));

        brandRepository.delete(brand);
        log.info("Brand deleted successfully: {}", id);
    }
}
