package com.farmted.auctionservice.observer;

import com.farmted.auctionservice.domain.Auction;
import com.farmted.auctionservice.feignClient.AuctionToProductFeignClient;
import com.farmted.auctionservice.observer.AuctionClosedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionStatusChangeListener {

    private final AuctionToProductFeignClient feignClient;

    @EventListener
    public void handleAuctionStatusChangeEvent(AuctionClosedEvent event) {
        Auction auction = (Auction) event.getSource();
        // Feign Client를 통한 API 호출
        feignClient.closedAuctionFromProduct(auction.getProductUuid());

    }
}
