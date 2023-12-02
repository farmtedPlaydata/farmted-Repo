package com.farmted.productservice.dto.response;

import com.farmted.productservice.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
