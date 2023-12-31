package com.farmted.productservice.dto.response;

import com.farmted.productservice.domain.Product;
import com.farmted.productservice.vo.ResponseAuctionGetVo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class SaleProductTypeResponseDto {
    private String name;
    private int stock;
    private Integer price;
    private String source;
    private String image;
    private Boolean status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer auctionPrice; // 경매 가격

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime auctionDeadline; // 경매 종료 시간

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private Boolean auctionStatus; // false: 경매중, true:  경매 종료

    public void mergeAuction(ResponseAuctionGetVo responseAuctionGetVo){
        auctionPrice = responseAuctionGetVo.auctionPrice();
        auctionDeadline = responseAuctionGetVo.auctionDeadline();
//        auctionStatus = responseAuctionGetVo.auctionStatus();

    }

    public SaleProductTypeResponseDto(Product product) {
        this.name = product.getName();
        this.stock = product.getStock();
        this.price = product.getPrice();
        this.source = product.getSource();
        this.image = product.getImage();
        this.status = product.isStatus();
        //this.auctionStatus = product.isAuctionStatus();
    }
}
