package com.farmted.auctionservice.dto.responseAuctionDto;

import com.farmted.auctionservice.domain.Auction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter @AllArgsConstructor @NoArgsConstructor @Builder
public class AuctionGetResponseDto {
    private Integer auctionPrice; // 경매 가격
    private LocalDate auctionDeadline; // 경매 종료
    private String productUuid;
    private boolean auctionStatus;

    public AuctionGetResponseDto(Auction auction){
        auctionPrice = auction.getAuctionPrice();
        auctionDeadline = auction.getAuctionDeadline();
        productUuid = auction.getProductUuid();
        auctionStatus = auction.getAuctionStatus();
    }
}
