package com.farmted.productservice.dto.response;

import com.farmted.productservice.domain.Product;
import com.farmted.productservice.vo.ResponseAuctionGetVo;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

public class SaleAuctionTypeResponseDto {
    private String name;
    private int stock;
    private Integer price;
    private String source;
    private String image;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer auctionPrice; // 경매 가격

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate auctionDeadline; // 경매 종료 시간

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String auctionBuyer;  // 낙찰자

    public void mergeAuction(ResponseAuctionGetVo responseAuctionGetVo){
        auctionPrice = responseAuctionGetVo.auctionPrice();
        auctionDeadline = responseAuctionGetVo.auctionDeadline();
        auctionBuyer = responseAuctionGetVo.auctionBuyer();

    }

    public SaleAuctionTypeResponseDto(Product product) {
        this.name = product.getName();
        this.stock = product.getStock();
        this.price = product.getPrice();
        this.source = product.getSource();
        this.image = product.getImage();
    }
}
