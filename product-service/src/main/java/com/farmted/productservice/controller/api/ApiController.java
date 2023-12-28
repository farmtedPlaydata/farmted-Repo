package com.farmted.productservice.controller.api;

import com.farmted.productservice.dto.response.ProductResponseDto;
import com.farmted.productservice.service.AuctionService;
import com.farmted.productservice.service.ProductService;
import com.farmted.productservice.util.GlobalResponseDto;
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
    private final ProductService productService;

    // 상품 DB에 있는 경매 상태 값을 종료 상태로 변경
    @PostMapping("/{productUuid}/endAuctions")
    @Operation(summary = "경매 상태 받아옴", description = "상품 DB에 있는 경매 상태 값을 종료 상태로 변경")
    public ResponseEntity<?> closedAuctionFromProduct(@PathVariable String productUuid){
        auctionService.endAuctionFromProduct(productUuid);
        return ResponseEntity.ok("상태값 변경 완료");
    }

    // 상품 상세 조회
    @GetMapping("/products/{board_uuid}/products")
    @Operation(summary = "상품 세부 내역 조회")
    public ResponseEntity<?> getProductDetail(@PathVariable (value = "board_uuid") String boardUuid){
        ProductResponseDto productDetail = productService.getProductDetail(boardUuid);
        return ResponseEntity.ok(GlobalResponseDto.of(productDetail));

    }


}
