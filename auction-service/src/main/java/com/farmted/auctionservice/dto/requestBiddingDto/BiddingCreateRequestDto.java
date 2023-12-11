package com.farmted.auctionservice.dto.requestBiddingDto;

import com.farmted.auctionservice.domain.Bidding;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter @AllArgsConstructor @NoArgsConstructor @Builder
public class BiddingCreateRequestDto {
    private double biddingPrice;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double biddingAutoPrice; // 자동 입찰

    public Bidding toEntity(String boardUuid,String memberUuid){
        return Bidding.builder()
                .biddingPrice(biddingPrice)
                .boardUuid(boardUuid)
                .memberUuid(memberUuid)
                .build();
    }


}
