package com.ecommerce.productservice.modules.brand;

import com.ecommerce.productservice.common.api.PageResponse;
import com.ecommerce.productservice.common.exception.BadRequestException;
import com.ecommerce.productservice.common.exception.ResourceNotFoundException;
import com.ecommerce.productservice.modules.brand.dto.BrandResponse;
import com.ecommerce.productservice.modules.brand.dto.CreateBrandRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PageResponse<BrandResponse> getAllBrands(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BrandEntity> brandPage = brandRepository.findAll(pageable);

        return buildPageResponse(brandPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<BrandResponse> getActiveBrands(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BrandEntity> brandPage = brandRepository.findByActiveTrue(pageable);

        return buildPageResponse(brandPage);
    }

    private PageResponse<BrandResponse> buildPageResponse(Page<BrandEntity> brandPage) {
        List<BrandResponse> content = brandMapper.toResponseList(brandPage.getContent());

        return PageResponse.<BrandResponse>builder()
                .content(content)
                .page(brandPage.getNumber())
                .size(brandPage.getSize())
                .totalElements(brandPage.getTotalElements())
                .totalPages(brandPage.getTotalPages())
                .last(brandPage.isLast())
                .first(brandPage.isFirst())
                .build();
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
