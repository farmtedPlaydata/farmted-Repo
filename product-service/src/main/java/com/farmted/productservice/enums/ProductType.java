package com.farmted.productservice.enums;

import com.farmted.productservice.exception.ProductException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {
    SALE("Sale", "판매"),
    AUCTION("Auction","경매"),
    PRODUCT("Product", "상품(판매+경매)");

    private final String typeEn;
    private final String typeKo;

    // String -> enum
    @JsonCreator
    public static ProductType fromLabel(String checkType) {
        for (ProductType type : ProductType.values()) {
            if (type.typeKo.equalsIgnoreCase(checkType)||type.typeEn.equalsIgnoreCase(checkType)) {
                return type;
            }
        }
        throw new ProductException();
    }



}
