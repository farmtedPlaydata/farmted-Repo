package com.farmted.auctionservice.controller;

import com.farmted.auctionservice.dto.responseAuctionDto.AuctionBuyerResponseDto;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionSellerResponseDto;
import com.farmted.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("auction-service")
public class AuctionController {

    private final AuctionService auctionService;

    // 경매 상세 내역 조회
    @GetMapping("/{boardUuid}/board")
    public ResponseEntity<?> getAuction(@PathVariable String boardUuid){

    }

    // 경매 내역 조회 -> 상태값 입력 받기(진행중/ 진행+종료 경매)
    @GetMapping("/Auctions/{AuctionStatus}")
    public ResponseEntity<?> getAuctionIng(@PathVariable Boolean AuctionStatus){

    }


    // 판매자 -> 낙찰 내역 조회 -> memberUuid
    @GetMapping("/seller/{memberUuid}/board")
    public ResponseEntity<?> findAuctionToSeller(
            @PathVariable String memberUuid
    ){
        List<AuctionBuyerResponseDto> auctionBuyerList = auctionService.auctionBuyerList(memberUuid);
        return ResponseEntity.ok(auctionBuyerList);
    }

    // 구매자  ->  낙찰 내역 조회 -> auctionBuyer
    @GetMapping("/{auctionBuyer}/board")
        public ResponseEntity<?> findAuctionTrue(
                @PathVariable String auctionBuyer
    ){
        List<AuctionSellerResponseDto> auctionSellerList = auctionService.auctionTrueList(auctionBuyer);
        return ResponseEntity.ok(auctionSellerList);
    }

}
