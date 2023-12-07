package com.farmted.auctionservice.dto.ResponseBiddingDto;

import com.farmted.auctionservice.domain.Bidding;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter @AllArgsConstructor @NoArgsConstructor
public class BiddingResponseDto {
    private Long biddingPrice; // 일반 입찰 금액
    private LocalDateTime biddingTime; // 입찰 시간
    private String memberUuid; // 응찰자

    public BiddingResponseDto(Bidding bidding){
        biddingPrice = bidding.getBiddingPrice();
        biddingTime=bidding.getBiddingTime();
        memberUuid = bidding.getMemberUuid();
    }
}
