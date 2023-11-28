package com.farmted.auctionservice.dto.requestDto;

import com.farmted.auctionservice.domain.Auction;
import lombok.*;

import java.time.LocalDate;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class AuctionCreateRequestDto {

    private int auctionPrice; // 가격
    private LocalDate auctionDeadline; // 경매 종료 시간


    public Auction toEntity(String memberUuid, String boardUuid){
        return Auction.builder()
                .auctionPrice(this.auctionPrice)
                .auctionDeadline(auctionDeadline)
                .memberUuid(memberUuid)
                .boardUuid(boardUuid)
                .build();
    }
}
