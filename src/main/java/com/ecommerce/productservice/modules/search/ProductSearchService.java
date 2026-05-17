package com.ecommerce.productservice.modules.search;

import com.ecommerce.productservice.common.api.PageResponse;
import com.ecommerce.productservice.common.pagination.PageParams;
import com.ecommerce.productservice.common.pagination.PageResponses;
import com.ecommerce.productservice.common.pagination.PageableFactory;
import com.ecommerce.productservice.common.pagination.SortFields;
import com.ecommerce.productservice.document.ProductDocument;
import com.ecommerce.productservice.modules.search.dto.ProductSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "app.search", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;

    public PageResponse<ProductDocument> searchProducts(ProductSearchRequest request) {
        log.info("Searching products with keyword: {}", request.getKeyword());

        PageParams pageParams = new PageParams()
                .page(request.getPage() != null ? request.getPage() : 0)
                .size(request.getSize() != null ? request.getSize() : 20)
                .sortBy(request.getSortBy() != null ? request.getSortBy() : "createdAt")
                .sortDirection(request.getSortDirection() != null ? request.getSortDirection() : "desc");

        Pageable pageable = PageableFactory.sorted(
                pageParams.getPage(),
                pageParams.getSize(),
                pageParams.getSortBy(),
                pageParams.getSortDirection(),
                SortFields.PRODUCT_DOCUMENT);

        Page<ProductDocument> resultPage = resolveSearchPage(request, pageable);
        return PageResponses.of(resultPage, resultPage.getContent());
    }

    private Page<ProductDocument> resolveSearchPage(ProductSearchRequest request, Pageable pageable) {
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            return productSearchRepository.findByNameContainingOrDescriptionContaining(
                    request.getKeyword(), request.getKeyword(), pageable);
        }
        if (request.getCategoryId() != null) {
            return productSearchRepository.findByCategoryId(
                    request.getCategoryId().toString(), pageable);
        }
        if (request.getBrandId() != null) {
            return productSearchRepository.findByBrandId(
                    request.getBrandId().toString(), pageable);
        }
        if (request.getMinPrice() != null && request.getMaxPrice() != null) {
            return productSearchRepository.findByPriceBetween(
                    request.getMinPrice(), request.getMaxPrice(), pageable);
        }
        if (Boolean.TRUE.equals(request.getFeatured())) {
            return productSearchRepository.findByFeaturedTrue(pageable);
        }
        return productSearchRepository.findByPublishedTrue(pageable);
    }
}
