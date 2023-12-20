package com.farmted.auctionservice.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "PRODUCT-SERVICE", path = "product-api")
public interface AuctionToProductFeignClient {
    @PostMapping("{productUuid}/endAuctions")
    public void closedAuctionFromProduct(@PathVariable String productUuid);

}
