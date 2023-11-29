package com.farmted.productservice.vo;

import com.farmted.productservice.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter
public class RequestAuctionCreateVo {
    private int price;
    private LocalDateTime startPeriod;

    public RequestAuctionCreateVo(Product product){
        price = product.getPrice();
        startPeriod = product.getCreateAt();
    }


}
