package com.farmted.auctionservice.controller.api;

import com.farmted.auctionservice.dto.requestAuctionDto.AuctionCreateRequestDto;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionGetResponseDto;
import com.farmted.auctionservice.service.AuctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Api는 product관련
@RestController
@RequiredArgsConstructor
@Tag(name = "auction feign API", description = "auction-service와 product-service 통신을 위한 API")
@RequestMapping("auction-api")
public class ApiController {

    private final AuctionService auctionService;

// 경매 정보 생성 및 시작
    @PostMapping(value = "/product/auctions")
    @Operation(summary = "경매 정보 생성 및 시작", description = "product-service 통신으로 경매 정보가 생성되고 경매가 바로 시작됩니다.")
    public ResponseEntity<?> createAuction(
            @RequestHeader("UUID") String memberUuid,
            @RequestBody AuctionCreateRequestDto auctionCreateRequestDto){
        auctionService.createAuction(auctionCreateRequestDto,memberUuid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


// 경매 내역 전달용 API
    @GetMapping(value ="/products/auctions")
    @Operation(summary = "경매 내역을 전달", description = "product-service에게 경매 정보를 전달합니다.")
    public ResponseEntity<?> auctionProductList(){
        List<AuctionGetResponseDto> auctionGetResponse = auctionService.getAuctionProductList();
        return ResponseEntity.ok(auctionGetResponse);
    }

// 구매자  ->  낙찰 내역 조회 -> auctionBuyer -> 마이페이지에서 조회(상품 정보 필요함)
    @GetMapping("/{auctionBuyer}/board")
    @Operation(summary = "구매자 낙찰 내역 조회", description = "구매자가 낙찰한 내역을 product-service에게 전달합니다.")
    public ResponseEntity<?> findAuctionBuyer(
            @PathVariable String auctionBuyer
    ){
        List<AuctionGetResponseDto> auctionSellerList = auctionService.auctionBuyerList(auctionBuyer);
        return ResponseEntity.ok(auctionSellerList);
    }

}
