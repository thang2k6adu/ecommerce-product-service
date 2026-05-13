package com.ecommerce.productservice.modules.search;

import com.ecommerce.productservice.common.api.PageResponse;
import com.ecommerce.productservice.document.ProductDocument;
import com.ecommerce.productservice.modules.search.dto.ProductSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "app.search", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;

    public PageResponse<ProductDocument> searchProducts(ProductSearchRequest request) {
        log.info("Searching products with keyword: {}", request.getKeyword());

        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 20;
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "createdAt";
        String sortDirection = request.getSortDirection() != null ? request.getSortDirection() : "desc";

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductDocument> resultPage;

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            resultPage = productSearchRepository.findByNameContainingOrDescriptionContaining(
                    request.getKeyword(), request.getKeyword(), pageable);
        } else if (request.getCategoryId() != null) {
            resultPage = productSearchRepository.findByCategoryId(
                    request.getCategoryId().toString(), pageable);
        } else if (request.getBrandId() != null) {
            resultPage = productSearchRepository.findByBrandId(
                    request.getBrandId().toString(), pageable);
        } else if (request.getMinPrice() != null && request.getMaxPrice() != null) {
            resultPage = productSearchRepository.findByPriceBetween(
                    request.getMinPrice(), request.getMaxPrice(), pageable);
        } else if (Boolean.TRUE.equals(request.getFeatured())) {
            resultPage = productSearchRepository.findByFeaturedTrue(pageable);
        } else {
            resultPage = productSearchRepository.findByPublishedTrue(pageable);
        }

        return buildPageResponse(resultPage);
    }

    public PageResponse<ProductDocument> searchProductsAdvanced(String keyword, int page, int size) {
        log.info("Advanced search for: {}", keyword);

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductDocument> resultPage = productSearchRepository
                .findByNameContainingOrDescriptionContaining(keyword, keyword, pageable);

        return buildPageResponse(resultPage);
    }

    private PageResponse<ProductDocument> buildPageResponse(Page<ProductDocument> documentPage) {
        return PageResponse.<ProductDocument>builder()
                .content(documentPage.getContent())
                .page(documentPage.getNumber())
                .size(documentPage.getSize())
                .totalElements(documentPage.getTotalElements())
                .totalPages(documentPage.getTotalPages())
                .last(documentPage.isLast())
                .first(documentPage.isFirst())
                .build();
    }
}
