package com.farmted.boardservice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

// 경매 데이터의 경우, 상품 데이터가 자동으로 작성해줄 것이고
    // 게시글UUID를 통해 데이터 요청만 할 예정이므로 게시글UUID를 넣지 않음
@Builder
public record AuctionVo(
     @JsonProperty("auctionPrice") int auctionPrice,
     @JsonProperty("auctionBuyer") String auctionBuyer,
     @JsonProperty("auctionDeadline") LocalDateTime auctionDeadline,
     @JsonProperty("auctionStatus") Boolean auctionStatus
    ){
}
