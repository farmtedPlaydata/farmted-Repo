package com.farmted.productservice.vo;

import com.farmted.productservice.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter
public class RequestAuctionCreateVo {
    // 이름이 달라서 안 되네?
    private int price;
    private LocalDate startPeriod;
    private String boardUuid;

    public RequestAuctionCreateVo(Product product){
        price = product.getPrice();
        startPeriod = product.getCreateAt().toLocalDate();
        boardUuid=product.getBoardUuid();
    }


}
