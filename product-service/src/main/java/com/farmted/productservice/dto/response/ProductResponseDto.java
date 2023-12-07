package com.farmted.productservice.dto.response;

import com.farmted.productservice.domain.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter @Setter @Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private String name;
    private int stock;
    private int price;
    private String source;
    private String image;
    private boolean status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean auctionStatus;

    public ProductResponseDto(Product product) {
        this.name = product.getName();
        this.stock = product.getStock();
        this.price = product.getPrice();
        this.source = product.getSource();
        this.image = product.getImage();
        this.status = product.isStatus();
        this.auctionStatus = product.isAuctionStatus();
    }
}
