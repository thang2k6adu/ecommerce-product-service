package com.ecommerce.productservice.modules.cart.dto.response;

import com.ecommerce.productservice.common.api.PageMeta;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartWithItemsPage {

    private CartResponse cart;
    private PageMeta itemsMeta;
}
