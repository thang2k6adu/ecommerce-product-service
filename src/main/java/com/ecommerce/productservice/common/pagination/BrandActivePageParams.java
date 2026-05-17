package com.ecommerce.productservice.common.pagination;

import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@ParameterObject
public class BrandActivePageParams extends PageParams {

    public BrandActivePageParams() {
        sortBy("name");
        sortDirection("asc");
    }
}
