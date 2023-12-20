package com.farmted.productservice.controller.api;

import com.farmted.productservice.service.AuctionService;
import com.farmted.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Api는 Auction관련

@RestController
@RequiredArgsConstructor
@Tag(name = "Auction-service feign 통신 API")
@RequestMapping("product-api")
public class ApiController {


    AuctionService auctionService;

    // 상품 DB에 있는 경매 상태 값을 종료 상태로 변경
    @Operation(summary = "경매 상태 받아옴", description = "상품 DB에 있는 경매 상태 값을 종료 상태로 변경")
    @Scheduled(cron ="*/60 * * * * *")
    @GetMapping("/endAuctions")
    public ResponseEntity<?> endAuctionToProduct(){
       auctionService.endAuctionFromProduct();
       return ResponseEntity.ok().build();
    }


}
