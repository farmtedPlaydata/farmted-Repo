package com.farmted.auctionservice.feignClient;

import com.farmted.auctionservice.vo.ProductVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "PRODUCT-SERVICE", path = "product-api")
public interface AuctionToProductFeignClient {
    @PostMapping("{productUuid}/endAuctions")
    public void closedAuctionFromProduct(@PathVariable String productUuid);

    @GetMapping("/products/{board_uuid}/products")
    public ProductVo getProductsDetail(@PathVariable (value = "board_uuid") String boardUuid);
}
