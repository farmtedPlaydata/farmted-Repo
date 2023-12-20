package com.farmted.auctionservice.controller;

import com.farmted.auctionservice.dto.ResponseBiddingDto.BiddingResponseDto;
import com.farmted.auctionservice.dto.requestBiddingDto.BiddingCreateRequestDto;
import com.farmted.auctionservice.service.BiddingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("bidding-service")
@RestController
@RequiredArgsConstructor
@Tag(name = "bidding API", description = "사용자 입찰 부분 API+ Bidding-Service API")
public class BiddingController {

    private final BiddingService biddingService;

    // 입찰 신청
    @PostMapping("/bid/{board_uuid}")
    public ResponseEntity<?> createBidding(
            @RequestBody BiddingCreateRequestDto biddingCreateRequestDto,
            @RequestHeader ("UUID") String memberUuid,
            @PathVariable ("board_uuid") String boardUuid
            ){
        biddingService.createBidding(biddingCreateRequestDto,boardUuid,memberUuid);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    //입찰 신청 목록 조회 + 상품 내역 필요함
    @GetMapping("/{memberUuid}/bidding")
    public ResponseEntity<?> getListBidding(@PathVariable String memberUuid){
        List<BiddingResponseDto> biddingList = biddingService.getBiddingList(memberUuid);
        return ResponseEntity.ok(biddingList);
    }


}
