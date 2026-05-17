package com.ecommerce.productservice.modules.category;

import com.ecommerce.productservice.common.api.PageResponse;
import com.ecommerce.productservice.common.exception.BadRequestException;
import com.ecommerce.productservice.common.exception.ResourceNotFoundException;
import com.ecommerce.productservice.common.pagination.PageParams;
import com.ecommerce.productservice.common.pagination.PageResponses;
import com.ecommerce.productservice.common.pagination.PageableFactory;
import com.ecommerce.productservice.common.pagination.SortFields;
import com.ecommerce.productservice.modules.category.dto.CategoryResponse;
import com.ecommerce.productservice.modules.category.dto.CreateCategoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CreateCategoryRequest request) {
        log.info("Creating category: {}", request.getName());

        if (request.getSlug() != null && categoryRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Category slug already exists: " + request.getSlug());
        }

        CategoryEntity category = categoryMapper.toEntity(request);

        if (request.getParentId() != null) {
            CategoryEntity parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category", "id", request.getParentId()));
            category.setParent(parent);
        }

        CategoryEntity savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with ID: {}", savedCategory.getId());

        return categoryMapper.toResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(UUID id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        CategoryResponse response = categoryMapper.toResponse(category);

        if (!category.getChildren().isEmpty()) {
            response.setChildren(categoryMapper.toResponseList(category.getChildren()));
        }

        return response;
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryBySlug(String slug) {
        CategoryEntity category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "slug", slug));

        CategoryResponse response = categoryMapper.toResponse(category);

        if (!category.getChildren().isEmpty()) {
            response.setChildren(categoryMapper.toResponseList(category.getChildren()));
        }

        return response;
    }

    @Transactional(readOnly = true)
    public PageResponse<CategoryResponse> getAllCategories(PageParams pageParams) {
        Pageable pageable = PageableFactory.sorted(
                pageParams.getPage(),
                pageParams.getSize(),
                pageParams.getSortBy(),
                pageParams.getSortDirection(),
                SortFields.CATEGORY);
        Page<CategoryEntity> categoryPage = categoryRepository.findAll(pageable);
        return PageResponses.of(categoryPage, categoryMapper.toResponseList(categoryPage.getContent()));
    }

    @Transactional(readOnly = true)
    public PageResponse<CategoryResponse> getActiveCategories(PageParams pageParams) {
        Pageable pageable = PageableFactory.sorted(
                pageParams.getPage(),
                pageParams.getSize(),
                pageParams.getSortBy(),
                pageParams.getSortDirection(),
                SortFields.CATEGORY);
        Page<CategoryEntity> categoryPage = categoryRepository.findByActiveTrue(pageable);
        return PageResponses.of(categoryPage, categoryMapper.toResponseList(categoryPage.getContent()));
    }

    @Transactional(readOnly = true)
    public PageResponse<CategoryResponse> getRootCategories(PageParams pageParams) {
        Sort sort = Sort.by("displayOrder").ascending().and(Sort.by("name").ascending());
        Pageable pageable = PageableFactory.sorted(pageParams.getPage(), pageParams.getSize(), sort);
        Page<CategoryEntity> categoryPage = categoryRepository.findByParentIsNull(pageable);
        return PageResponses.of(categoryPage, mapRootCategoriesWithChildren(categoryPage.getContent()));
    }

    private List<CategoryResponse> mapRootCategoriesWithChildren(List<CategoryEntity> categories) {
        return categories.stream()
                .map(category -> {
                    CategoryResponse response = categoryMapper.toResponse(category);
                    if (!category.getChildren().isEmpty()) {
                        response.setChildren(categoryMapper.toResponseList(category.getChildren()));
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoryTree() {
        List<CategoryEntity> rootCategories = categoryRepository.findRootCategoriesActive();
        return buildCategoryTree(rootCategories);
    }

    private List<CategoryResponse> buildCategoryTree(List<CategoryEntity> categories) {
        return categories.stream()
                .map(category -> {
                    CategoryResponse response = categoryMapper.toResponse(category);
                    if (!category.getChildren().isEmpty()) {
                        List<CategoryEntity> activeChildren = category.getChildren().stream()
                                .filter(CategoryEntity::getActive)
                                .collect(Collectors.toList());
                        response.setChildren(buildCategoryTree(activeChildren));
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }

    public CategoryResponse updateCategory(UUID id, CreateCategoryRequest request) {
        log.info("Updating category: {}", id);

        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (request.getSlug() != null && !request.getSlug().equals(category.getSlug())) {
            if (categoryRepository.existsBySlug(request.getSlug())) {
                throw new BadRequestException("Category slug already exists: " + request.getSlug());
            }
        }

        categoryMapper.updateEntityFromRequest(request, category);

        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                throw new BadRequestException("Category cannot be its own parent");
            }
            CategoryEntity parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category", "id", request.getParentId()));
            category.setParent(parent);
        }

        CategoryEntity updatedCategory = categoryRepository.save(category);
        log.info("Category updated successfully: {}", id);

        return categoryMapper.toResponse(updatedCategory);
    }

    public void deleteCategory(UUID id) {
        log.info("Deleting category: {}", id);

        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (!category.getChildren().isEmpty()) {
            throw new BadRequestException("Cannot delete category with child categories");
        }

        categoryRepository.delete(category);
        log.info("Category deleted successfully: {}", id);
    }
}
