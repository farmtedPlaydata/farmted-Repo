package com.farmted.productservice.vo;

import java.time.LocalDateTime;


public record ResponseAuctionGetVo (
        Integer auctionPrice, // 경매 가격 == 낙찰 가격
        LocalDateTime auctionDeadline, // 경매 종료
        String productUuid,
        String auctionBuyer,  // 낙찰자
        boolean auctionStatus){ }
