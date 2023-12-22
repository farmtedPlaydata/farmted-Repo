package com.farmted.auctionservice.dto.responseAuctionDto;

import com.farmted.auctionservice.domain.Auction;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @AllArgsConstructor @NoArgsConstructor @Builder
public class AuctionGetResponseDto {
    private BigDecimal auctionPrice; // 경매 가격 == 낙찰 가격
    private LocalDateTime auctionDeadline; // 경매 종료
    private String productUuid;
    private boolean auctionStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String auctionBuyer;  // 낙찰자

    public AuctionGetResponseDto(Auction auction){
        auctionPrice = auction.getAuctionPrice();
        auctionDeadline = auction.getAuctionDeadline();
        productUuid = auction.getProductUuid();
        auctionStatus = auction.getAuctionStatus();
        auctionBuyer=auction.getAuctionBuyer();
    }
}
