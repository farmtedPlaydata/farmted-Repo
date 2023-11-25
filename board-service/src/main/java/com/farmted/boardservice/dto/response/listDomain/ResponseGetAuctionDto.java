package com.farmted.boardservice.dto.response.listDomain;

import com.farmted.boardservice.vo.AuctionVo;

import java.time.LocalDateTime;

public class ResponseGetAuctionDto {
    // 경매용 정보
    private int auctionPrice;
    private LocalDateTime auctionDeadline;

    public ResponseGetAuctionDto(AuctionVo auctionVo) {
        this.auctionPrice = auctionPrice;
        this.auctionDeadline = auctionDeadline;
    }
}
