package com.farmted.productservice.dto.request;

import com.farmted.productservice.domain.Product;
import lombok.*;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
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
                .uuid(UUID.randomUUID().toString())
                .name(this.getName())
                .stock(this.getStock())
                .price(this.getPrice())
                .source(this.getSource())
                .image(this.getImage())
                .memberUuid(memberUuid)
                .boardUuid(this.getBoardUuid())
                .status(true) // 생성 시 기본 값 true : 판매 자동 시작
                .auctionStatus(true) // 생성 시 기본 값 true : 경매 자동 시작
                .build();
    }

}
