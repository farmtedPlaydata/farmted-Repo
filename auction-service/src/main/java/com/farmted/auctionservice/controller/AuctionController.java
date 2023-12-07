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


    // 판매자 -> 낙찰 내역 조회 -> memberUuid
    //TODO: PathVariable이 아니라 HEADER로 받는 건가?
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
