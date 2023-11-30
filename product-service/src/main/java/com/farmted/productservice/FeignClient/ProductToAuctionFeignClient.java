package com.farmted.productservice.FeignClient;

import com.farmted.productservice.vo.RequestAuctionCreateVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "AUCTION-SERVICE", path = "auction-service")
public interface ProductToAuctionFeignClient {
    @PostMapping("/product/auctions")
    public void createProductToAuctionFeign(
            @RequestHeader String productUuid,
            @RequestHeader("UUID") String memberUuid,
            @RequestBody RequestAuctionCreateVo auctionCreateVo);
}
