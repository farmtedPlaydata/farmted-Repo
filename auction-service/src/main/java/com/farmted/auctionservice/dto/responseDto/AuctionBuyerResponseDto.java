package com.farmted.auctionservice.dto.responseDto;


import com.farmted.auctionservice.domain.Auction;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AuctionBuyerResponseDto {
    private int auctionPrice; // 가격

    private String auctionBuyer;  // 낙찰자

    private LocalDate auctionDeadline; // 경매 종료 시간

    private Boolean auctionStatus; // 0: 경매중, 1:  경매 종료

    private String memberUuid;

    public AuctionBuyerResponseDto(Auction auction){
        this.auctionPrice=auction.getAuctionPrice();
        this.auctionBuyer =auction.getAuctionBuyer();
        this.auctionDeadline=auction.getAuctionDeadline();
        this.auctionStatus =auction.getAuctionStatus();
        this.memberUuid =auction.getMemberUuid();
    }
}
