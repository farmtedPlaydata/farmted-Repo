package com.farmted.productservice.FeignClient;

import com.farmted.productservice.vo.RequestAuctionCreateVo;
import com.farmted.productservice.vo.ResponseAuctionGetVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "auction-service", path = "auction-service")
public interface ProductToAuctionFeignClient {
    @PostMapping("/auction-api/product/auctions")
    public void createProductToAuctionFeign(
            @RequestHeader("UUID") String memberUuid,
            @RequestBody RequestAuctionCreateVo auctionCreateVo);


    @GetMapping(value ="/auction-api/products/auctions")
    public List<ResponseAuctionGetVo> auctionProductList();

    @GetMapping("/auction-api/{auctionBuyer}")
    public List<ResponseAuctionGetVo> findAuctionBuyer(@PathVariable String auctionBuyer);
}

