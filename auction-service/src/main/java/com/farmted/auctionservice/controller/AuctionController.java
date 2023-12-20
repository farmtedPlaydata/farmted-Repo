package com.farmted.auctionservice.controller;

import com.farmted.auctionservice.dto.responseAuctionDto.AuctionBoardResponseDto;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionGetResponseDto;
import com.farmted.auctionservice.service.AuctionService;
import com.farmted.auctionservice.util.GlobalResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// board 통신 관련

@RestController
@RequiredArgsConstructor
@RequestMapping("auction-service")
@Tag(name = "auction API", description = "auction-service와 board-service 통신을 위한 API")
public class AuctionController {

    private final AuctionService auctionService;


// 판매자 -> 경매목록 조회 -> memberUuid
    @GetMapping("/seller/{memberUuid}/board")
    @Operation(summary = "판매자 경매 목록 조회 ", description = "판매자가 등록한 경매목록을 조회(낙찰자 포함)합니다.")
    public ResponseEntity<?> findAuctionToSeller(
            @PathVariable String memberUuid,
            @RequestParam int pageNo
    ){
        List<AuctionBoardResponseDto> auctionBuyerList = auctionService.auctionSellerList(memberUuid,pageNo);
        return ResponseEntity.ok(GlobalResponseDto.listOf(auctionBuyerList));
    }


// board 페이징 내역 조회
    @GetMapping("/auctions/board")
    @Operation(summary = "경매 목록 조회 ", description = "경매 DB에 있는 전체 목록을 조회합니다.")
    public ResponseEntity<?> getAuctionList(@RequestParam int pageNo){
        List<AuctionBoardResponseDto> auctionBoaderList = auctionService.getAuctionList(pageNo);
        return ResponseEntity.ok(GlobalResponseDto.listOf(auctionBoaderList));
    }

// 경매 내역 상세 조회
    @GetMapping("/auction/{boardUuid}/board")
    @Operation(summary = "경매 상세 내역 조회 ", description = "경매 DB 상세 내역을 조회합니다.")
    public ResponseEntity<?> getAuctionDetail(@PathVariable String boardUuid){
        AuctionBoardResponseDto auction = auctionService.getAuctionDetail(boardUuid);
        return ResponseEntity.ok(GlobalResponseDto.of(auction));
    }



}
