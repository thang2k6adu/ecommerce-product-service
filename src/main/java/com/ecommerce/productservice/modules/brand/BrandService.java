package com.ecommerce.productservice.modules.brand;

import com.ecommerce.productservice.common.api.PageResponse;
import com.ecommerce.productservice.common.exception.BadRequestException;
import com.ecommerce.productservice.common.exception.ResourceNotFoundException;
import com.ecommerce.productservice.common.pagination.PageParams;
import com.ecommerce.productservice.common.pagination.PageResponses;
import com.ecommerce.productservice.common.pagination.PageableFactory;
import com.ecommerce.productservice.common.pagination.SortFields;
import com.ecommerce.productservice.modules.brand.dto.BrandResponse;
import com.ecommerce.productservice.modules.brand.dto.CreateBrandRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PageResponse<BrandResponse> getAllBrands(PageParams pageParams) {
        Pageable pageable = PageableFactory.sorted(
                pageParams.getPage(),
                pageParams.getSize(),
                pageParams.getSortBy(),
                pageParams.getSortDirection(),
                SortFields.BRAND);
        Page<BrandEntity> brandPage = brandRepository.findAll(pageable);
        return PageResponses.of(brandPage, brandMapper.toResponseList(brandPage.getContent()));
    }

    @Transactional(readOnly = true)
    public PageResponse<BrandResponse> getActiveBrands(PageParams pageParams) {
        Pageable pageable = PageableFactory.sorted(
                pageParams.getPage(),
                pageParams.getSize(),
                pageParams.getSortBy(),
                pageParams.getSortDirection(),
                SortFields.BRAND);
        Page<BrandEntity> brandPage = brandRepository.findByActiveTrue(pageable);
        return PageResponses.of(brandPage, brandMapper.toResponseList(brandPage.getContent()));
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
