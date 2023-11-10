package com.farmted.boardservice.vo;

import lombok.Builder;

@Builder
public record ProductVo (
        String productName,
        int productStock,
        long productPrice,
        String productSource,
        String productImage
    ){
}
