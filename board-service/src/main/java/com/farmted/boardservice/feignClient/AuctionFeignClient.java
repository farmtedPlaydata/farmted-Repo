package com.farmted.boardservice.feignClient;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Auction-Service에 통신할 FeignClient
//@FeignClient(name = "auction-service", path= "/auction-service")
public interface AuctionFeignClient {
    // boardUuid를 통해 해당 경매 상태 반환
    @GetMapping("/auctions/{boardUuid}/boards")
    boolean getAuctionStatusByBoardUuid(@PathVariable String boardUuid);
}
