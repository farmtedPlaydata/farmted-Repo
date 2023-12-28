package com.farmted.auctionservice.controller;

import com.farmted.auctionservice.dto.ResponseBiddingDto.BiddingResponseDto;
import com.farmted.auctionservice.dto.requestAuctionDto.AuctionCreateRequestDto;
import com.farmted.auctionservice.dto.requestBiddingDto.BiddingCreateRequestDto;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionBoardResponseDto;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionGetResponseDto;
import com.farmted.auctionservice.service.AuctionService;
import com.farmted.auctionservice.service.BiddingService;
import com.farmted.auctionservice.util.GlobalResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final BiddingService biddingService;


    // 입찰 신청
    @PostMapping("/bidding-service/bid/{boardUuid}")
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
    @GetMapping("/bidding-service/{memberUuid}/bidding")
    @Operation(summary = "입찰 신청 목록 조회 ", description = "입찰 신청 목록을 신청한 상품 정보화 함께 출력합니다.")
    public ResponseEntity<?> getListBidding(@PathVariable String memberUuid){
        List<BiddingResponseDto> biddingList = biddingService.getBiddingList(memberUuid);
        return ResponseEntity.ok(biddingList);
    }

    // 경매 정보 생성 및 시작
    @PostMapping(value = "/auction-api/product/auctions")
    @Operation(summary = "경매 정보 생성 및 시작", description = "product-service 통신으로 경매 정보가 생성되고 경매가 바로 시작됩니다.")
    public ResponseEntity<?> createAuction(
            @RequestHeader("UUID") String memberUuid,
            @Valid @RequestBody AuctionCreateRequestDto auctionCreateRequestDto){
        auctionService.createAuction(auctionCreateRequestDto,memberUuid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    // 경매 내역 전달용 API
    @GetMapping(value ="/auction-api/products/auctions")
    @Operation(summary = "경매 내역을 전달", description = "product-service에게 경매 정보를 전달합니다.")
    public ResponseEntity<?> auctionProductList(){
        List<AuctionGetResponseDto> auctionGetResponse = auctionService.getAuctionProductList();
        return ResponseEntity.ok(auctionGetResponse);
    }

    // 구매자  ->  낙찰 내역 조회 -> auctionBuyer -> 마이페이지에서 조회(상품 정보 필요함)
    @GetMapping("/auction-api/{auctionBuyer}")
    @Operation(summary = "구매자 낙찰 내역 조회", description = "구매자가 낙찰한 내역을 product-service에게 전달합니다.")
    public ResponseEntity<?> findAuctionBuyer(
            @PathVariable String auctionBuyer
    ){
        List<AuctionGetResponseDto> auctionSellerList = auctionService.auctionBuyerList(auctionBuyer);
        return ResponseEntity.ok(auctionSellerList);
    }

}
