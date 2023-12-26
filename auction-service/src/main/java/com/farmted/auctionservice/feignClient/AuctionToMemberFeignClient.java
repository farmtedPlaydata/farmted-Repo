package com.farmted.auctionservice.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "member-service", path = "/member-service")
public interface AuctionToMemberFeignClient {
    @PutMapping("/after-bid/{uuid}")
    public void failedBidBalance(@PathVariable String uuid, int balance); //MemberUuid
}
