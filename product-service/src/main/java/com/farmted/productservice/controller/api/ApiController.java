package com.farmted.productservice.controller.api;

import com.farmted.productservice.service.AuctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

// Api는 Auction관련

@RestController
@RequiredArgsConstructor
@Tag(name = "Auction-service feign 통신 API")
@RequestMapping("product-api")
public class ApiController {


    private final AuctionService auctionService;


    // 상품 DB에 있는 경매 상태 값을 종료 상태로 변경
    @PostMapping("/{productUuid}/endAuctions")
    @Operation(summary = "경매 상태 받아옴", description = "상품 DB에 있는 경매 상태 값을 종료 상태로 변경")
    public ResponseEntity<?> closedAuctionFromProduct(@PathVariable String productUuid){
        auctionService.endAuctionFromProduct(productUuid);
        return ResponseEntity.ok("상태값 변경 완료");
    }


}
