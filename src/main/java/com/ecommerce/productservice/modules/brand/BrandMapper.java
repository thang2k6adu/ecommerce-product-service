package com.ecommerce.productservice.modules.brand;

import com.ecommerce.productservice.modules.brand.dto.BrandResponse;
import com.ecommerce.productservice.modules.brand.dto.CreateBrandRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BrandMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BrandEntity toEntity(CreateBrandRequest request);

    BrandResponse toResponse(BrandEntity brand);

    List<BrandResponse> toResponseList(List<BrandEntity> brands);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(CreateBrandRequest request, @MappingTarget BrandEntity brand);
}
