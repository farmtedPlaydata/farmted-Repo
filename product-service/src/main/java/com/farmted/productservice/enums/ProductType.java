package com.farmted.productservice.enums;

import lombok.Getter;

@Getter
public enum ProductType {
    SALE("Sale", "판매"),
    PRODUCT("Product", "상품(판매+경매)");

    private String typeEn;
    private String typeKo;

    ProductType(String typeEn, String typeKo) {
        this.typeEn = typeEn;
        this.typeKo = typeKo;
    }
}
