package com.ecommerce.productservice.mapper;

import com.ecommerce.productservice.dto.request.CreateCategoryRequest;
import com.ecommerce.productservice.dto.response.CategoryResponse;
import com.ecommerce.productservice.entity.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-12T21:35:13+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CreateCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.active( request.getActive() );
        category.description( request.getDescription() );
        category.displayOrder( request.getDisplayOrder() );
        category.imageUrl( request.getImageUrl() );
        category.metaDescription( request.getMetaDescription() );
        category.metaKeywords( request.getMetaKeywords() );
        category.metaTitle( request.getMetaTitle() );
        category.name( request.getName() );
        category.slug( request.getSlug() );

        return category.build();
    }

    @Override
    public CategoryResponse toResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();

        categoryResponse.parentId( categoryParentId( category ) );
        categoryResponse.parentName( categoryParentName( category ) );
        categoryResponse.active( category.getActive() );
        categoryResponse.children( toResponseList( category.getChildren() ) );
        categoryResponse.createdAt( category.getCreatedAt() );
        categoryResponse.description( category.getDescription() );
        categoryResponse.displayOrder( category.getDisplayOrder() );
        categoryResponse.id( category.getId() );
        categoryResponse.imageUrl( category.getImageUrl() );
        categoryResponse.metaDescription( category.getMetaDescription() );
        categoryResponse.metaKeywords( category.getMetaKeywords() );
        categoryResponse.metaTitle( category.getMetaTitle() );
        categoryResponse.name( category.getName() );
        categoryResponse.slug( category.getSlug() );
        categoryResponse.updatedAt( category.getUpdatedAt() );

        return categoryResponse.build();
    }

    @Override
    public List<CategoryResponse> toResponseList(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryResponse> list = new ArrayList<CategoryResponse>( categories.size() );
        for ( Category category : categories ) {
            list.add( toResponse( category ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(CreateCategoryRequest request, Category category) {
        if ( request == null ) {
            return;
        }

        if ( request.getActive() != null ) {
            category.setActive( request.getActive() );
        }
        if ( request.getDescription() != null ) {
            category.setDescription( request.getDescription() );
        }
        if ( request.getDisplayOrder() != null ) {
            category.setDisplayOrder( request.getDisplayOrder() );
        }
        if ( request.getImageUrl() != null ) {
            category.setImageUrl( request.getImageUrl() );
        }
        if ( request.getMetaDescription() != null ) {
            category.setMetaDescription( request.getMetaDescription() );
        }
        if ( request.getMetaKeywords() != null ) {
            category.setMetaKeywords( request.getMetaKeywords() );
        }
        if ( request.getMetaTitle() != null ) {
            category.setMetaTitle( request.getMetaTitle() );
        }
        if ( request.getName() != null ) {
            category.setName( request.getName() );
        }
        if ( request.getSlug() != null ) {
            category.setSlug( request.getSlug() );
        }
    }

    private UUID categoryParentId(Category category) {
        if ( category == null ) {
            return null;
        }
        Category parent = category.getParent();
        if ( parent == null ) {
            return null;
        }
        UUID id = parent.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String categoryParentName(Category category) {
        if ( category == null ) {
            return null;
        }
        Category parent = category.getParent();
        if ( parent == null ) {
            return null;
        }
        String name = parent.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
