package com.farmted.productservice.FeignClient;

import com.farmted.productservice.vo.RequestAuctionCreateVo;
import com.farmted.productservice.vo.ResponseAuctionEndVo;
import com.farmted.productservice.vo.ResponseAuctionGetVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "AUCTION-SERVICE", path = "auction-api")
public interface ProductToAuctionFeignClient {
    @PostMapping("/product/auctions")
    public void createProductToAuctionFeign(
            @RequestHeader("UUID") String memberUuid,
            @RequestBody RequestAuctionCreateVo auctionCreateVo);


    @GetMapping("/products/auctions")
    public List<ResponseAuctionEndVo> endAuctionFromProduct();

    @GetMapping("/{productUuid}/auctions")
    public ResponseAuctionGetVo getAuctionIng(@PathVariable String productUuid);
}
