package com.farmted.productservice.vo;

import com.farmted.productservice.domain.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter
public class RequestAuctionCreateVo {
    // @JsonProperty로 vo, dto 전달명 통일
    private @JsonProperty("auctionPrice") int price;
    private @JsonProperty("auctionDeadline") LocalDate startPeriod;
    private String boardUuid;
    private String productUuid;


    public RequestAuctionCreateVo(Product product){
        price = product.getPrice();
        startPeriod = product.getCreateAt().toLocalDate();
        boardUuid=product.getBoardUuid();
        productUuid = product.getUuid();
    }


}
