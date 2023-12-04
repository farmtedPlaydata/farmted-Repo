package com.farmted.auctionservice.controller;

import com.farmted.auctionservice.dto.requestDto.AuctionCreateRequestDto;
import com.farmted.auctionservice.dto.responseDto.AuctionBuyerResponseDto;
import com.farmted.auctionservice.dto.responseDto.AuctionSellerResponseDto;
import com.farmted.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("auction-service")
public class AuctionController {

    private final AuctionService auctionService;

    // 경매 상세 내역 조회

    // 경매 중인 내역 조회 -> 경매 진행 상태

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
