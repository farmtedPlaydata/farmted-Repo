package com.farmted.productservice.dto.request;


import com.farmted.productservice.domain.Product;
import com.farmted.productservice.enums.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductSaveRequestDto(
        @NotBlank(message = "상품명은 필수 입력 값입니다.")
        String name,
        @Positive(message = "수량은 양수 값만 가능합니다..")
        int stock,
        @NotNull(message = "상품 가격은 필수 입력값 입니다.")
        @PositiveOrZero(message = "상품 가격은 0 이상이어야 합니다.")
        int price,
        @NotBlank(message = "원산지는 필수 입력 값입니다.")
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

