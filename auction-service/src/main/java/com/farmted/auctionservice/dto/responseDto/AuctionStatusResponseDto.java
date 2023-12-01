package com.farmted.auctionservice.dto.responseDto;

import com.farmted.auctionservice.domain.Auction;

import java.time.LocalDate;

public class AuctionStatusResponseDto {
    private String productUuid;
    private LocalDate auctionDeadline; // 경매 종료 시간
    private Boolean auctionStatus; // 0: 경매중, 1:  경매 종료

    public AuctionStatusResponseDto(Auction auction){
        productUuid =auction.getProductUuid();
        auctionDeadline =auction.getAuctionDeadline();
        auctionStatus =auction.getAuctionStatus();
    }
}
