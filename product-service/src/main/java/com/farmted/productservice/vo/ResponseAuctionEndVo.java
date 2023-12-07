package com.farmted.productservice.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ResponseAuctionEndVo {
    private int auctionPrice; // 가격
    private String auctionBuyer;  // 낙찰자
    private String productUuid;
    private LocalDate auctionDeadline; // 경매 종료 시간
    private Boolean auctionStatus; // 0: 경매중, 1:  경매 종료

}
