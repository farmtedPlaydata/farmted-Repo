package com.farmted.productservice.vo;

import com.farmted.productservice.domain.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter
public class RequestAuctionCreateVo {
    // @JsonProperty로 vo, dto 전달명 통일
    private @JsonProperty("auctionPrice") Integer price;
    private @JsonProperty("auctionDeadline") LocalDateTime startPeriod;
    private String boardUuid;
    private String productUuid;

    public RequestAuctionCreateVo(Product product){
        price = product.getPrice();
        startPeriod = product.getCreateAt();
        boardUuid=product.getBoardUuid();
        productUuid = product.getUuid();
    }
}
