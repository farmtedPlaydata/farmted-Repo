package com.farmted.productservice.dto.request;


import com.farmted.productservice.domain.Product;
import com.farmted.productservice.enums.ProductType;
public record ProductSaveRequestDto(
        String name,
        int stock,
        int price,
        String source,
        String image,
        String boardUuid,
        ProductType productType
) {
    public Product toEntity(String memberUuid){
        return Product.builder()
                .name(this.name())
                .stock(this.stock())
                .price(this.price())
                .source(this.source())
                .image(this.image())
                .memberUuid(memberUuid)
                .boardUuid(this.boardUuid())
                .productType(productType)
                .build();
    }
}

