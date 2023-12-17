package com.farmted.productservice.dto.response;

import com.farmted.productservice.domain.Product;
import com.farmted.productservice.vo.ResponseAuctionGetVo;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

public class ProductAuctionResponseDto {
    private String name;
    private int stock;
    private int price;
    private String source;
    private String image;
    private Boolean status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer auctionPrice; // 경매 가격

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate auctionDeadline; // 경매 종료

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean auctionStatus; // false: 경매중, true:  경매 종료

    public ProductAuctionResponseDto(Product product, ResponseAuctionGetVo responseAuctionGetVo){
        this.name = product.getName();
        this.stock = product.getStock();
        this.price = product.getPrice();
        this.source = product.getSource();
        this.image = product.getImage();
        this.status = product.isStatus();
        this.auctionStatus = product.isAuctionStatus();
        this.auctionDeadline =responseAuctionGetVo.getAuctionDeadline();
        this.auctionPrice = responseAuctionGetVo.getAuctionPrice();
    }

    public ProductAuctionResponseDto(Product product) {
        this.name = product.getName();
        this.stock = product.getStock();
        this.price = product.getPrice();
        this.source = product.getSource();
        this.image = product.getImage();
        this.status = product.isStatus();
        this.auctionStatus = product.isAuctionStatus();
    }
}
