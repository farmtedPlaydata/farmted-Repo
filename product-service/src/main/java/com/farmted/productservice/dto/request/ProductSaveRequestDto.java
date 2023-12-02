package com.farmted.productservice.dto.request;


import com.farmted.productservice.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

