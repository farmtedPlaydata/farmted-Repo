package com.farmted.productservice.dto.request;


import com.farmted.productservice.domain.Product;
import com.farmted.productservice.enums.ProductType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class ProductSaveRequestDto{
        @NotBlank(message = "상품명은 필수 입력 값입니다.")
        String name;
        @Positive(message = "수량은 양수 값만 가능합니다.")
        int stock;
        @DecimalMin(value = "0", inclusive = true, message = "상품 가격은 0원 이상이어야 합니다.")
        @NotNull(message = "상품 가격은 필수 입력값 입니다.")
        Integer price;
        @NotBlank(message = "원산지는 필수 입력 값입니다.")
        String source;
        String image;
        String boardUuid;
        ProductType productType;

    public Product toEntity(String memberUuid){
        return Product.builder()
                .name(name)
                .stock(stock)
                .price(price)
                .source(source)
                .image(image)
                .memberUuid(memberUuid)
                .boardUuid(boardUuid)
                .productType(productType)
                .build();
    }

}

