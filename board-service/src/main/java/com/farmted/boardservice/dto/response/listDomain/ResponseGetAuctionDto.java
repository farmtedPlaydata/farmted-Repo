package com.farmted.boardservice.dto.response.listDomain;

import com.farmted.boardservice.vo.AuctionVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseGetAuctionDto {
    // 경매용 정보 (null이면 직렬화 대상에서 제외)
    @JsonProperty("auctionPrice")
    private Integer auctionPrice;
    @JsonProperty("auctionDeadline")
    private LocalDateTime auctionDeadline;
    @JsonProperty("buyerUuid")
    private String buyerUuid;

    public ResponseGetAuctionDto(AuctionVo auctionVo) {
        this.auctionPrice = auctionVo.auctionPrice();
        this.auctionDeadline = auctionVo.auctionDeadline();
        this.buyerUuid = auctionVo.auctionBuyer();
    }
}