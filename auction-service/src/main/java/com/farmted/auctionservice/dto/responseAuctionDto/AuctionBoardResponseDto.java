package com.farmted.auctionservice.dto.responseAuctionDto;


import com.farmted.auctionservice.domain.Auction;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class

AuctionBoardResponseDto {
    private int auctionPrice; // 가격

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String auctionBuyer;  // 낙찰자

    private LocalDate auctionDeadline; // 경매 종료 시간

    private Boolean auctionStatus; // 0: 경매중, 1:  경매 종료



    public AuctionBoardResponseDto(Auction auction){
        this.auctionPrice=auction.getAuctionPrice();
        this.auctionDeadline=auction.getAuctionDeadline();
        this.auctionStatus =auction.getAuctionStatus();
        //this.memberUuid =auction.getMemberUuid();
    }
}
