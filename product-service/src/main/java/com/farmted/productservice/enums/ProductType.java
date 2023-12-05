package com.farmted.productservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {
    SALE("Sale", "판매"),
    PRODUCT("Product", "상품(판매+경매)");

    private final String typeEn;
    private final String typeKo;


}
