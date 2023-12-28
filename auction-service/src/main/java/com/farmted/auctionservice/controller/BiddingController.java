package com.farmted.auctionservice.controller;

import com.farmted.auctionservice.domain.Bidding;
import com.farmted.auctionservice.dto.ResponseBiddingDto.BiddingResponseDto;
import com.farmted.auctionservice.dto.requestBiddingDto.BiddingCreateRequestDto;
import com.farmted.auctionservice.service.BiddingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("bidding-service")
@RestController
@RequiredArgsConstructor
@Tag(name = "bidding API", description = "사용자 입찰 부분 API+ Bidding-Service API")
@CrossOrigin("*")
public class BiddingController {

    private final BiddingService biddingService;

    // 입찰 신청
    @PostMapping("/bid/{boardUuid}")
    @Operation(summary = "입찰 신청 ", description = "희망가를 필수로 입력받아 입찰을 신청합니다.")
        public ResponseEntity<?> createBidding(
            @Valid @RequestBody BiddingCreateRequestDto biddingCreateRequestDto,
            @RequestHeader ("UUID") String memberUuid,
            @PathVariable ("boardUuid") String boardUuid
            ){
        biddingService.createBidding(biddingCreateRequestDto,boardUuid,memberUuid);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    //입찰 신청 목록 조회 + 상품 내역 필요함
    @GetMapping("/{memberUuid}/bidding")
    @Operation(summary = "입찰 신청 목록 조회 ", description = "입찰 신청 목록을 신청한 상품 정보화 함께 출력합니다.")
    public ResponseEntity<?> getListBidding(@PathVariable String memberUuid){
        List<BiddingResponseDto> biddingList = biddingService.getBiddingList(memberUuid);
        return ResponseEntity.ok(biddingList);
    }


}
