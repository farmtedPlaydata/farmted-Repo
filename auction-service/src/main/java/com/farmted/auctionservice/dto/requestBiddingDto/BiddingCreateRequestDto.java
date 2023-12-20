package com.farmted.auctionservice.dto.requestBiddingDto;

import com.farmted.auctionservice.domain.Bidding;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @AllArgsConstructor @NoArgsConstructor @Builder @Setter
public class BiddingCreateRequestDto {
    private BigDecimal biddingPrice;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer biddingAutoPrice; // 자동 입찰

    public Bidding toEntity(String boardUuid,String memberUuid){
        return Bidding.builder()
                .biddingPrice(biddingPrice)
                .boardUuid(boardUuid)
                .memberUuid(memberUuid)
                .biddingTime(LocalDateTime.now())
                .biddingUuid(UUID.randomUUID().toString())
                .build();
    }


}
