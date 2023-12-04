package com.farmted.auctionservice.dto.responseAuctionDto;

import com.farmted.auctionservice.domain.Auction;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AuctionResponseDto {
    private Integer auctionPrice; // 경매 가격
    private LocalDate auctionDeadline; // 경매 종료
    private Boolean auctionStatus; // false: 경매중, true:  경매 종료

    public AuctionResponseDto(Auction auction){
        auctionPrice = auction.getAuctionPrice();
        auctionDeadline = auction.getAuctionDeadline();
        auctionStatus =auction.getAuctionStatus();
    }
}
