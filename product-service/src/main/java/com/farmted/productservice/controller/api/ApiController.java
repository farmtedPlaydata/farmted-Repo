package com.farmted.productservice.controller.api;

import com.farmted.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("product-service")
public class ApiController {

    private final ProductService productService;

    @Scheduled(cron ="*/60 * * * * *")
    @GetMapping("/endAuctions")
    public ResponseEntity<?> endAuctionToProduct(){
       productService.endAuctionFromProduct();
       return ResponseEntity.ok().build();
    }

}
