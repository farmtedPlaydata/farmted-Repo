package com.farmted.productservice.controller.api;

import com.farmted.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
