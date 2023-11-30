package com.farmted.auctionservice.dto.requestDto;

import com.farmted.auctionservice.domain.Auction;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class AuctionCreateRequestDto {

    private int auctionPrice; // 가격
    private LocalDate auctionDeadline; // 경매 종료 시간
    private String boardUuid;


    public Auction toEntity(String memberUuid,LocalDate actionDeadline){
        return Auction.builder()
                .auctionPrice(this.auctionPrice)
                .auctionDeadline(auctionDeadline)
                .memberUuid(memberUuid)
                .boardUuid(this.boardUuid)
                .build();
    }
}
