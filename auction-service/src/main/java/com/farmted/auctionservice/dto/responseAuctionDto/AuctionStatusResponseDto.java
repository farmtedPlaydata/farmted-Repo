package com.farmted.auctionservice.dto.responseAuctionDto;

import com.farmted.auctionservice.domain.Auction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionStatusResponseDto {
    private String productUuid;
    private LocalDate auctionDeadline; // 경매 종료 시간
    private Boolean auctionStatus; // 0: 경매중, 1:  경매 종료

    private Integer auctionPrice; // 가격
    private String auctionBuyer;  // 낙찰자


    public AuctionStatusResponseDto(Auction auction){
        this.productUuid =auction.getProductUuid();
        auctionDeadline =auction.getAuctionDeadline();
        auctionStatus =auction.getAuctionStatus();
        auctionPrice =auction.getAuctionPrice();
        auctionBuyer = auction.getAuctionBuyer();
    }
}
