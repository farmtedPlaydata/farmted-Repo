package com.farmted.auctionservice.dto.responseAuctionDto;

import com.farmted.auctionservice.domain.Auction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionStatusResponseDto {
    private String productUuid;
    private LocalDateTime auctionDeadline; // 경매 종료 시간
    private BigDecimal auctionPrice; // 가격
    private String auctionBuyer;  // 낙찰자


    public AuctionStatusResponseDto(Auction auction){
        this.productUuid =auction.getProductUuid();
        auctionDeadline =auction.getAuctionDeadline();
        auctionPrice =auction.getAuctionPrice();
        auctionBuyer = auction.getAuctionBuyer();

    }
}
