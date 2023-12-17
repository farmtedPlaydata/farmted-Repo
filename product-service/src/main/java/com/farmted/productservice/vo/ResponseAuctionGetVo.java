package com.farmted.productservice.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter @AllArgsConstructor @NoArgsConstructor
public class ResponseAuctionGetVo {
    private Integer auctionPrice; // 경매 가격
    private LocalDate auctionDeadline; // 경매 종료
    private String productUuid;

}
