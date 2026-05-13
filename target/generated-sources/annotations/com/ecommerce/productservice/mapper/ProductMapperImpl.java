package com.ecommerce.productservice.mapper;

import com.ecommerce.productservice.dto.request.CreateProductRequest;
import com.ecommerce.productservice.dto.response.ProductResponse;
import com.ecommerce.productservice.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-12T21:35:13+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ProductImageMapper productImageMapper;
    @Autowired
    private ProductVariantMapper productVariantMapper;

    @Override
    public Product toEntity(CreateProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.barcode( request.getBarcode() );
        product.compareAtPrice( request.getCompareAtPrice() );
        product.costPrice( request.getCostPrice() );
        product.description( request.getDescription() );
        product.dimensionUnit( request.getDimensionUnit() );
        product.featured( request.getFeatured() );
        product.height( request.getHeight() );
        product.length( request.getLength() );
        product.metaDescription( request.getMetaDescription() );
        product.metaKeywords( request.getMetaKeywords() );
        product.metaTitle( request.getMetaTitle() );
        product.name( request.getName() );
        product.price( request.getPrice() );
        product.published( request.getPublished() );
        product.shortDescription( request.getShortDescription() );
        product.sku( request.getSku() );
        product.slug( request.getSlug() );
        product.status( request.getStatus() );
        product.stockQuantity( request.getStockQuantity() );
        product.weight( request.getWeight() );
        product.weightUnit( request.getWeightUnit() );
        product.width( request.getWidth() );

        return product.build();
    }

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.barcode( product.getBarcode() );
        productResponse.brand( brandMapper.toResponse( product.getBrand() ) );
        productResponse.category( categoryMapper.toResponse( product.getCategory() ) );
        productResponse.compareAtPrice( product.getCompareAtPrice() );
        productResponse.costPrice( product.getCostPrice() );
        productResponse.createdAt( product.getCreatedAt() );
        productResponse.description( product.getDescription() );
        productResponse.dimensionUnit( product.getDimensionUnit() );
        productResponse.featured( product.getFeatured() );
        productResponse.height( product.getHeight() );
        productResponse.id( product.getId() );
        productResponse.images( productImageMapper.toResponseList( product.getImages() ) );
        productResponse.length( product.getLength() );
        productResponse.metaDescription( product.getMetaDescription() );
        productResponse.metaKeywords( product.getMetaKeywords() );
        productResponse.metaTitle( product.getMetaTitle() );
        productResponse.name( product.getName() );
        productResponse.price( product.getPrice() );
        productResponse.published( product.getPublished() );
        productResponse.publishedAt( product.getPublishedAt() );
        productResponse.shortDescription( product.getShortDescription() );
        productResponse.sku( product.getSku() );
        productResponse.slug( product.getSlug() );
        productResponse.status( product.getStatus() );
        productResponse.stockQuantity( product.getStockQuantity() );
        productResponse.updatedAt( product.getUpdatedAt() );
        productResponse.variants( productVariantMapper.toResponseList( product.getVariants() ) );
        productResponse.weight( product.getWeight() );
        productResponse.weightUnit( product.getWeightUnit() );
        productResponse.width( product.getWidth() );

        return productResponse.build();
    }

    @Override
    public List<ProductResponse> toResponseList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductResponse> list = new ArrayList<ProductResponse>( products.size() );
        for ( Product product : products ) {
            list.add( toResponse( product ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(CreateProductRequest request, Product product) {
        if ( request == null ) {
            return;
        }

        if ( request.getBarcode() != null ) {
            product.setBarcode( request.getBarcode() );
        }
        if ( request.getCompareAtPrice() != null ) {
            product.setCompareAtPrice( request.getCompareAtPrice() );
        }
        if ( request.getCostPrice() != null ) {
            product.setCostPrice( request.getCostPrice() );
        }
        if ( request.getDescription() != null ) {
            product.setDescription( request.getDescription() );
        }
        if ( request.getDimensionUnit() != null ) {
            product.setDimensionUnit( request.getDimensionUnit() );
        }
        if ( request.getFeatured() != null ) {
            product.setFeatured( request.getFeatured() );
        }
        if ( request.getHeight() != null ) {
            product.setHeight( request.getHeight() );
        }
        if ( request.getLength() != null ) {
            product.setLength( request.getLength() );
        }
        if ( request.getMetaDescription() != null ) {
            product.setMetaDescription( request.getMetaDescription() );
        }
        if ( request.getMetaKeywords() != null ) {
            product.setMetaKeywords( request.getMetaKeywords() );
        }
        if ( request.getMetaTitle() != null ) {
            product.setMetaTitle( request.getMetaTitle() );
        }
        if ( request.getName() != null ) {
            product.setName( request.getName() );
        }
        if ( request.getPrice() != null ) {
            product.setPrice( request.getPrice() );
        }
        if ( request.getPublished() != null ) {
            product.setPublished( request.getPublished() );
        }
        if ( request.getShortDescription() != null ) {
            product.setShortDescription( request.getShortDescription() );
        }
        if ( request.getSku() != null ) {
            product.setSku( request.getSku() );
        }
        if ( request.getSlug() != null ) {
            product.setSlug( request.getSlug() );
        }
        if ( request.getStatus() != null ) {
            product.setStatus( request.getStatus() );
        }
        if ( request.getStockQuantity() != null ) {
            product.setStockQuantity( request.getStockQuantity() );
        }
        if ( request.getWeight() != null ) {
            product.setWeight( request.getWeight() );
        }
        if ( request.getWeightUnit() != null ) {
            product.setWeightUnit( request.getWeightUnit() );
        }
        if ( request.getWidth() != null ) {
            product.setWidth( request.getWidth() );
        }
    }
}
