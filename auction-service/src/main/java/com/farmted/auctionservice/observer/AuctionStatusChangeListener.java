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
        // Feign Client를 통한 API 호출-> 경매 종료 전달
        feignClient.closedAuctionFromProduct(auction.getProductUuid());

        // 경매 종료 시 낙찰 실패자 잔고 수정 로직

        // 낙찰자 낙찰 완료 알림

    }
}
