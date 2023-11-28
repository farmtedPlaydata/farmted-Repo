package com.farmted.productservice.controller.api;

import com.farmted.productservice.service.ProductService;
import com.farmted.productservice.vo.RequestAuctionCreateVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final ProductService productService;

    @PostMapping("/products/{productUuid}/auction")
    public ResponseEntity<?> createProductToAuction(@PathVariable String productUuid){
        productService.createProductToAuction(productUuid);
        return ResponseEntity.ok("통신 완료");
    }

}
