package com.farmted.boardservice.vo;

import java.time.LocalDateTime;

// 경매 데이터의 경우, 상품 데이터가 자동으로 작성해줄 것이고
    // 게시글UUID를 통해 데이터 요청만 할 예정이므로 게시글UUID를 넣지 않음
public record AuctionVo(
     int auctionPrice,
     String auctionBidder,
     String auctionBuyer,
     LocalDateTime auctionDeadline,
     Boolean auctionStatus
    ){

    // %%% 테스트용 더미데이터 생성 메소드 (삭제 예정)
    public static AuctionVo createDummyAuction(String boardUuid) {
        // 여기서 더미 데이터를 생성하고 반환
        return new AuctionVo(
                1_000,
                "Dummy Bidder",
                "Dummy Buyer",
                LocalDateTime.now().plusDays(7),
                true
        );
    }
}
