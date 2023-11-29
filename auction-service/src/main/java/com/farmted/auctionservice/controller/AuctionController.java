package com.farmted.auctionservice.controller;

import com.farmted.auctionservice.dto.requestDto.AuctionCreateRequestDto;
import com.farmted.auctionservice.dto.responseDto.AuctionBuyerResponseDto;
import com.farmted.auctionservice.dto.responseDto.AuctionSellerResponseDto;
import com.farmted.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("auction-service")
public class AuctionController {

    private final AuctionService auctionService;

    // 경매 정보 생성 및 시작
    @PostMapping(value = "/auctions/products")
    public ResponseEntity<?> createAuction(
            @RequestHeader String memberUuid,
            @RequestHeader String boardUuid,
            AuctionCreateRequestDto auctionCreateRequestDto){
        auctionService.createAuction(auctionCreateRequestDto, memberUuid,boardUuid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 판매자 낙찰 내역 조회
    @GetMapping("/seller/{memberUuid}/board")
    public ResponseEntity<?> findAuctionToSeller(
            @PathVariable String memberUuid
    ){
        List<AuctionBuyerResponseDto> auctionBuyerList = auctionService.auctionBuyerList(memberUuid);
        return ResponseEntity.ok(auctionBuyerList);
    }

    // 구매자 낙찰 내역 조회
    @GetMapping("/{memberUuid}/board")
        public ResponseEntity<?> findAuctionTrue(
                @PathVariable String memberUuid
    ){
        List<AuctionSellerResponseDto> auctionSellerList = auctionService.auctionTrueList(memberUuid);
        return ResponseEntity.ok(auctionSellerList);
    }

}
