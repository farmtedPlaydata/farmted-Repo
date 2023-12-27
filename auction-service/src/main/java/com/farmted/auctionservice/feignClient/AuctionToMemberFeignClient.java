package com.farmted.auctionservice.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member-service", path = "/member-service")
public interface AuctionToMemberFeignClient {
    @PutMapping("/after-bid/{uuid}")
    public void failedBidBalance(@PathVariable String uuid, int balance); //MemberUuid

    @PutMapping("/after-bid/{uuid}")
    public ResponseEntity<?> afterBidBalance(@PathVariable String uuid, @RequestBody int balance);
}
