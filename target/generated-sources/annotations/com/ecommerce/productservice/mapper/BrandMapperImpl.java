package com.ecommerce.productservice.mapper;

import com.ecommerce.productservice.dto.request.CreateBrandRequest;
import com.ecommerce.productservice.dto.response.BrandResponse;
import com.ecommerce.productservice.entity.Brand;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-12T21:35:13+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class BrandMapperImpl implements BrandMapper {

    @Override
    public Brand toEntity(CreateBrandRequest request) {
        if ( request == null ) {
            return null;
        }

        Brand.BrandBuilder brand = Brand.builder();

        brand.active( request.getActive() );
        brand.description( request.getDescription() );
        brand.logoUrl( request.getLogoUrl() );
        brand.metaDescription( request.getMetaDescription() );
        brand.metaTitle( request.getMetaTitle() );
        brand.name( request.getName() );
        brand.slug( request.getSlug() );
        brand.websiteUrl( request.getWebsiteUrl() );

        return brand.build();
    }

    @Override
    public BrandResponse toResponse(Brand brand) {
        if ( brand == null ) {
            return null;
        }

        BrandResponse.BrandResponseBuilder brandResponse = BrandResponse.builder();

        brandResponse.active( brand.getActive() );
        brandResponse.createdAt( brand.getCreatedAt() );
        brandResponse.description( brand.getDescription() );
        brandResponse.id( brand.getId() );
        brandResponse.logoUrl( brand.getLogoUrl() );
        brandResponse.metaDescription( brand.getMetaDescription() );
        brandResponse.metaTitle( brand.getMetaTitle() );
        brandResponse.name( brand.getName() );
        brandResponse.slug( brand.getSlug() );
        brandResponse.updatedAt( brand.getUpdatedAt() );
        brandResponse.websiteUrl( brand.getWebsiteUrl() );

        return brandResponse.build();
    }

    @Override
    public List<BrandResponse> toResponseList(List<Brand> brands) {
        if ( brands == null ) {
            return null;
        }

        List<BrandResponse> list = new ArrayList<BrandResponse>( brands.size() );
        for ( Brand brand : brands ) {
            list.add( toResponse( brand ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(CreateBrandRequest request, Brand brand) {
        if ( request == null ) {
            return;
        }

        if ( request.getActive() != null ) {
            brand.setActive( request.getActive() );
        }
        if ( request.getDescription() != null ) {
            brand.setDescription( request.getDescription() );
        }
        if ( request.getLogoUrl() != null ) {
            brand.setLogoUrl( request.getLogoUrl() );
        }
        if ( request.getMetaDescription() != null ) {
            brand.setMetaDescription( request.getMetaDescription() );
        }
        if ( request.getMetaTitle() != null ) {
            brand.setMetaTitle( request.getMetaTitle() );
        }
        if ( request.getName() != null ) {
            brand.setName( request.getName() );
        }
        if ( request.getSlug() != null ) {
            brand.setSlug( request.getSlug() );
        }
        if ( request.getWebsiteUrl() != null ) {
            brand.setWebsiteUrl( request.getWebsiteUrl() );
        }
    }
}
