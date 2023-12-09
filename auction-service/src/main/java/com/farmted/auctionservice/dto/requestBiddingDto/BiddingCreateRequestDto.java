package com.farmted.auctionservice.dto.requestBiddingDto;

import com.farmted.auctionservice.domain.Bidding;
import com.farmted.auctionservice.vo.RedisBidding;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter @AllArgsConstructor @NoArgsConstructor @Builder
public class BiddingCreateRequestDto {
    private Integer biddingPrice;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer biddingAutoPrice; // 자동 입찰

    public Bidding toEntity(String boardUuid,String memberUuid){
        return Bidding.builder()
                .biddingPrice(biddingPrice)
                .boardUuid(boardUuid)
                .memberUuid(memberUuid)
                .build();
    }

    public RedisBidding toRedisEntity(String boardUuid, String memberUuid){
        return RedisBidding.builder()
                .biddingPrice(biddingPrice)
                .boardUuid(boardUuid)
                .memberUuid(memberUuid)
                .biddingUuid(UUID.randomUUID().toString())
                .build();
    }
}
