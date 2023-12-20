package com.farmted.auctionservice.dto.requestAuctionDto;

import com.farmted.auctionservice.domain.Auction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class AuctionCreateRequestDto {

    private BigDecimal auctionPrice; // 가격
    private LocalDate auctionDeadline; // 경매 종료 시간
    private String boardUuid;
    private String productUuid;


    public Auction toEntity(String memberUuid,LocalDate actionDeadline){
        return Auction.builder()
                .auctionPrice(this.auctionPrice)
                .auctionDeadline(actionDeadline)
                .memberUuid(memberUuid)
                .boardUuid(this.boardUuid)
                .productUuid(this.productUuid)
                .build();
    }
}
