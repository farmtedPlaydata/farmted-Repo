package com.farmted.productservice.controller.api;

import com.farmted.productservice.dto.response.ProductAuctionResponseDto;
import com.farmted.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Api는 Auction관련

@RestController
@RequiredArgsConstructor
@RequestMapping("product-api")
public class ApiController {


    private final ProductService productService;

    // 경매 종료 상태로 변경
    @Scheduled(cron ="*/60 * * * * *")
    @GetMapping("/endAuctions")
    public ResponseEntity<?> endAuctionToProduct(){
       productService.endAuctionFromProduct();
       return ResponseEntity.ok().build();
    }

    // 경매 중인 내역 조회 -> PRODUCT
    @GetMapping("/products/auctions")
    public ResponseEntity<?> getProductAuctionList(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
    ){
        List<ProductAuctionResponseDto> listProductAuction = productService.getListProductAuction(pageNo);
        return ResponseEntity.ok(listProductAuction);
    }

}
