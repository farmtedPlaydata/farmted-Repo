package com.farmted.productservice.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


public record ResponseAuctionEndVo(
    int auctionPrice, // 가격
    String auctionBuyer,  // 낙찰자
    String productUuid,
    LocalDate auctionDeadline, // 경매 종료 시간
    Boolean auctionStatus // 0: 경매중, 1:  경매 종료

){}
