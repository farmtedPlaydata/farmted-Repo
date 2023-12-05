package com.farmted.auctionservice.dto.responseDto;

import com.farmted.auctionservice.domain.Auction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionStatusResponseDto {
    private int auctionPrice; // 가격
    private String auctionBuyer;  // 낙찰자
    private String productUuid;
    private LocalDate auctionDeadline; // 경매 종료 시간
    private Boolean auctionStatus; // 0: 경매중, 1:  경매 종료

    public AuctionStatusResponseDto(Auction auction){
        this.productUuid =auction.getProductUuid();
        this.auctionDeadline =auction.getAuctionDeadline();
        this.auctionStatus =auction.getAuctionStatus();
    }
}
