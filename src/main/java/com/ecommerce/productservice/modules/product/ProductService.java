package com.ecommerce.productservice.modules.product;

import com.ecommerce.productservice.document.ProductDocument;
import com.ecommerce.productservice.document.ProductDocumentMapper;
import com.ecommerce.productservice.modules.brand.BrandEntity;
import com.ecommerce.productservice.modules.brand.BrandRepository;
import com.ecommerce.productservice.modules.category.CategoryEntity;
import com.ecommerce.productservice.modules.category.CategoryRepository;
import com.ecommerce.productservice.modules.product.dto.*;
import com.ecommerce.productservice.modules.productimage.ProductImageEntity;
import com.ecommerce.productservice.modules.productimage.ProductImageMapper;
import com.ecommerce.productservice.modules.productimage.dto.CreateProductImageRequest;
import com.ecommerce.productservice.modules.productvariant.ProductVariantEntity;
import com.ecommerce.productservice.modules.productvariant.ProductVariantMapper;
import com.ecommerce.productservice.modules.productvariant.ProductVariantRepository;
import com.ecommerce.productservice.modules.productvariant.dto.CreateProductVariantRequest;
import com.ecommerce.productservice.common.api.PageResponse;
import com.ecommerce.productservice.common.exception.BadRequestException;
import com.ecommerce.productservice.common.exception.ResourceNotFoundException;
import com.ecommerce.productservice.common.pagination.PageParams;
import com.ecommerce.productservice.common.pagination.PageResponses;
import com.ecommerce.productservice.common.pagination.PageableFactory;
import com.ecommerce.productservice.common.pagination.SortFields;
import com.ecommerce.productservice.modules.search.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductVariantRepository productVariantRepository;
    private final Optional<ProductSearchRepository> productSearchRepository;

    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductVariantMapper productVariantMapper;
    private final ProductDocumentMapper productDocumentMapper;

    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("Creating product: {}", request.getName());

        if (request.getSlug() != null && productRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Product slug already exists: " + request.getSlug());
        }

        ProductEntity product = productMapper.toEntity(request);

        if (request.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        if (request.getBrandId() != null) {
            BrandEntity brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", request.getBrandId()));
            product.setBrand(brand);
        }

        if (Boolean.TRUE.equals(request.getPublished())) {
            product.setPublishedAt(LocalDateTime.now());
        }

        ProductEntity savedProduct = productRepository.save(product);

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (CreateProductImageRequest imageRequest : request.getImages()) {
                ProductImageEntity image = productImageMapper.toEntity(imageRequest);
                savedProduct.addImage(image);
            }
        }

        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            for (CreateProductVariantRequest variantRequest : request.getVariants()) {
                if (productVariantRepository.existsBySku(variantRequest.getSku())) {
                    throw new BadRequestException("Variant SKU already exists: " + variantRequest.getSku());
                }
                ProductVariantEntity variant = productVariantMapper.toEntity(variantRequest);
                savedProduct.addVariant(variant);
            }
        }

        savedProduct = productRepository.save(savedProduct);

        syncToElasticsearch(savedProduct);

        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return productMapper.toResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID id) {
        ProductEntity product = productRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return productMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductBySlug(String slug) {
        ProductEntity product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "slug", slug));
        return productMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getAllProducts(PageParams pageParams) {
        Pageable pageable = PageableFactory.sorted(
                pageParams.getPage(),
                pageParams.getSize(),
                pageParams.getSortBy(),
                pageParams.getSortDirection(),
                SortFields.PRODUCT);
        Page<ProductEntity> productPage = productRepository.findAll(pageable);
        return toProductPageResponse(productPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getPublishedProducts(PageParams pageParams) {
        Pageable pageable = PageableFactory.sorted(
                pageParams.getPage(),
                pageParams.getSize(),
                pageParams.getSortBy(),
                pageParams.getSortDirection(),
                SortFields.PRODUCT);
        Page<ProductEntity> productPage = productRepository.findByPublishedTrue(pageable);
        return toProductPageResponse(productPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getProductsByCategory(UUID categoryId, PageParams pageParams) {
        Pageable pageable = PageableFactory.unsorted(pageParams.getPage(), pageParams.getSize());
        Page<ProductEntity> productPage = productRepository.findPublishedByCategoryId(categoryId, pageable);
        return toProductPageResponse(productPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getProductsByBrand(UUID brandId, PageParams pageParams) {
        Pageable pageable = PageableFactory.unsorted(pageParams.getPage(), pageParams.getSize());
        Page<ProductEntity> productPage = productRepository.findPublishedByBrandId(brandId, pageable);
        return toProductPageResponse(productPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getFeaturedProducts(PageParams pageParams) {
        Pageable pageable = PageableFactory.unsorted(pageParams.getPage(), pageParams.getSize());
        Page<ProductEntity> productPage = productRepository.findByFeaturedTrue(pageable);
        return toProductPageResponse(productPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getProductsByPriceRange(
            BigDecimal minPrice, BigDecimal maxPrice, PageParams pageParams) {
        Pageable pageable = PageableFactory.unsorted(pageParams.getPage(), pageParams.getSize());
        Page<ProductEntity> productPage = productRepository.findByPriceRange(minPrice, maxPrice, pageable);
        return toProductPageResponse(productPage);
    }

    private PageResponse<ProductResponse> toProductPageResponse(Page<ProductEntity> productPage) {
        return PageResponses.of(productPage, productMapper.toResponseList(productPage.getContent()));
    }

    public ProductResponse updateProduct(UUID id, CreateProductRequest request) {
        log.info("Updating product: {}", id);

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (request.getSlug() != null && !request.getSlug().equals(product.getSlug())) {
            if (productRepository.existsBySlug(request.getSlug())) {
                throw new BadRequestException("Product slug already exists: " + request.getSlug());
            }
        }

        productMapper.updateEntityFromRequest(request, product);

        if (request.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        if (request.getBrandId() != null) {
            BrandEntity brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", request.getBrandId()));
            product.setBrand(brand);
        }

        if (Boolean.TRUE.equals(request.getPublished()) && product.getPublishedAt() == null) {
            product.setPublishedAt(LocalDateTime.now());
        }

        ProductEntity updatedProduct = productRepository.save(product);

        syncToElasticsearch(updatedProduct);

        log.info("Product updated successfully: {}", id);
        return productMapper.toResponse(updatedProduct);
    }

    public void deleteProduct(UUID id) {
        log.info("Deleting product: {}", id);

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productRepository.delete(product);

        productSearchRepository.ifPresent(repo -> {
            try {
                repo.deleteById(id.toString());
            } catch (Exception e) {
                log.error("Failed to remove product from Elasticsearch: {}", id, e);
            }
        });

        log.info("Product deleted successfully: {}", id);
    }

    public void syncAllProductsToElasticsearch() {
        if (productSearchRepository.isEmpty()) {
            log.info("Skipping Elasticsearch sync because search is disabled");
            return;
        }

        log.info("Syncing all products to Elasticsearch");

        List<ProductEntity> products = productRepository.findAll();
        List<ProductDocument> documents = productDocumentMapper.toDocumentList(products);
        productSearchRepository.get().saveAll(documents);

        log.info("Synced {} products to Elasticsearch", products.size());
    }

    private void syncToElasticsearch(ProductEntity product) {
        if (productSearchRepository.isEmpty()) {
            return;
        }

        try {
            ProductDocument document = productDocumentMapper.toDocument(product);
            productSearchRepository.get().save(document);
            log.debug("Product synced to Elasticsearch: {}", product.getId());
        } catch (Exception e) {
            log.error("Failed to sync product to Elasticsearch: {}", product.getId(), e);
        }
    }

}
