package com.farmted.boardservice.dto.response.detailDomain;

import com.farmted.boardservice.vo.AuctionVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class ResponseGetAuctionDetailDto {

    // 경매용 정보
    private int auctionPrice;
    private String auctionBuyer;
    private LocalDateTime auctionDeadline;
    // 경매 상태의 경우, GET 요청엔 필요하다 판단해서 추가
    private Boolean auctionStatus;

    // 경매 정보
    public ResponseGetAuctionDetailDto(AuctionVo auctionVo){
        this.auctionPrice = auctionVo.auctionPrice();
        this.auctionBuyer = auctionVo.auctionBuyer();
        this.auctionDeadline = auctionVo.auctionDeadline();
        this.auctionStatus = auctionVo.auctionStatus();
    }
}
