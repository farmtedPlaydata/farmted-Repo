package com.farmted.auctionservice.dto.responseDto;

import com.farmted.auctionservice.domain.Auction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionSellerResponseDto {
    private int auctionPrice; // 가격
    private String auctionBuyer;  // 낙찰자
    private String memberUuid;

    public AuctionSellerResponseDto(Auction auction){
        auctionPrice=auction.getAuctionPrice();
        auctionBuyer=auction.getAuctionBuyer();
        memberUuid=auction.getMemberUuid();
    }
}
