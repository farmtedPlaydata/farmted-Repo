package com.farmted.productservice.dto.request;


import com.farmted.productservice.domain.Product;
import com.farmted.productservice.enums.ProductType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSaveRequestDto {

    private String name;

    private int stock;

    private int price;

    private String source;

    private String image;

    private String boardUuid;

    private ProductType productType;

    public Product toEntity(String memberUuid){
        return Product.builder()
                .name(this.getName())
                .stock(this.getStock())
                .price(this.getPrice())
                .source(this.getSource())
                .image(this.getImage())
                .memberUuid(memberUuid)
                .boardUuid(this.getBoardUuid())
                .build();
    }

}

