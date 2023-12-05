package com.farmted.auctionservice.controller.api;

import com.farmted.auctionservice.dto.requestAuctionDto.AuctionCreateRequestDto;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionGetResponseDto;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionStatusResponseDto;
import com.farmted.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Api는 product관련

@RestController
@RequiredArgsConstructor
@RequestMapping("auction-service")
public class ApiController {

    private final AuctionService auctionService;

    // 경매 정보 생성 및 시작
    @PostMapping(value = "/product/auctions")
    public ResponseEntity<?> createAuction(
            @RequestHeader("UUID") String memberUuid,
            @RequestBody AuctionCreateRequestDto auctionCreateRequestDto){
        auctionService.createAuction(auctionCreateRequestDto,memberUuid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    // 경매 종료 전달
    @GetMapping(value = "/products/auctions")
    public ResponseEntity<?> endAuction(){
        List<AuctionStatusResponseDto> auctionStatusResponseDtoList = auctionService.endAuctions();
        return ResponseEntity.ok(auctionStatusResponseDtoList);
    }

    // 경매 내역 상세 조회
    @GetMapping("/{productUuid}/auctions")
    public ResponseEntity<?> getAuctionIng(@PathVariable String productUuid){
        AuctionGetResponseDto auction = auctionService.getAuctionList(productUuid);
        return ResponseEntity.ok(auction);
    }

}
