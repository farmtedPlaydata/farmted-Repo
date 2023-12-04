package com.farmted.productservice.FeignClient;

import com.farmted.productservice.vo.RequestAuctionCreateVo;
import com.farmted.productservice.vo.ResponseAuctionEndVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "AUCTION-SERVICE", path = "auction-service")
public interface ProductToAuctionFeignClient {
    @PostMapping("/product/auctions")
    public void createProductToAuctionFeign(
            @RequestHeader("UUID") String memberUuid,
            @RequestBody RequestAuctionCreateVo auctionCreateVo);


    @GetMapping("/products/auctions")
    public List<ResponseAuctionEndVo> endAuctionFromProduct();
}
