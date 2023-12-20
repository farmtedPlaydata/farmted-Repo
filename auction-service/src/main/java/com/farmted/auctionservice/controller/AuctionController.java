package com.farmted.auctionservice.controller;

import com.farmted.auctionservice.dto.responseAuctionDto.AuctionBoardResponseDto;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionGetResponseDto;
import com.farmted.auctionservice.service.AuctionService;
import com.farmted.auctionservice.util.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("auction-service")
public class AuctionController {

    private final AuctionService auctionService;


// 판매자 -> 경매목록 조회 -> memberUuid -> 낙찰 내역 조회
    @GetMapping("/seller/{memberUuid}/board")
    public ResponseEntity<?> findAuctionToSeller(
            @PathVariable String memberUuid,
            @RequestParam int pageNo
    ){
        List<AuctionBoardResponseDto> auctionBuyerList = auctionService.auctionBuyerList(memberUuid,pageNo);
        return ResponseEntity.ok(GlobalResponseDto.listOf(auctionBuyerList));
    }

// 얘도 상품이 같이 처리?
// 구매자  ->  낙찰 내역 조회 -> auctionBuyer -> 마이페이지에서 조회
    @GetMapping("/{auctionBuyer}/board")
        public ResponseEntity<?> findAuctionTrue(
                @PathVariable String auctionBuyer
    ){
        List<AuctionGetResponseDto> auctionSellerList = auctionService.auctionTrueList(auctionBuyer);
        return ResponseEntity.ok(GlobalResponseDto.listOf(auctionSellerList));
    }

// board 페이징 내역 조회
    @GetMapping("/auctions/board")
    public ResponseEntity<?> getAuctionList(@RequestParam int pageNo){
        List<AuctionBoardResponseDto> auctionBoaderList = auctionService.getAuctionList(pageNo);
        return ResponseEntity.ok(GlobalResponseDto.listOf(auctionBoaderList));
    }

// 경매 내역 상세 조회
    @GetMapping("/auction/{boardUuid}/board")
    public ResponseEntity<?> getAuctionDetail(@PathVariable String boardUuid){
        AuctionBoardResponseDto auction = auctionService.getAuctionDetail(boardUuid);
        return ResponseEntity.ok(GlobalResponseDto.of(auction));
    }
}
