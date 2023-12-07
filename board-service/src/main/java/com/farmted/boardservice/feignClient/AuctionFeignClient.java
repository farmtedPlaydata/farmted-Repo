package com.farmted.boardservice.feignClient;

import com.farmted.boardservice.util.GlobalResponseDto;
import com.farmted.boardservice.vo.AuctionVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "${service.auction.name}", path= "${service.auction.url}")
public interface AuctionFeignClient {
    // ResponseEntity<GlobalResponseDto<AuctionVo>>
    @GetMapping("/seller/{memberUuid}/board")
    ResponseEntity<GlobalResponseDto<List<AuctionVo>>> findAuctionToSeller(@PathVariable String memberUuid, int pageNo);

    // AUCTION 카테고리의 경우, 해당 페이지에 맞는 경매 리스트 반환
    @GetMapping("/auctions/board")
    ResponseEntity<GlobalResponseDto<List<AuctionVo>>> findAuctionList(@RequestParam int pageNo);

    // AUCTION의 상세정보 반환
    @GetMapping("/auction/{boardUuid}/board")
    ResponseEntity<GlobalResponseDto<AuctionVo>> findAuctionByBoardUuid(@PathVariable String boardUuid);
    // BoardUuid에 맞는 개별 경매 정보 반환
        //    private int auctionPrice;
        //    private String auctionBuyer;
        //    private LocalDateTime auctionDeadline;
}
