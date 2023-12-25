package com.farmted.auctionservice.observer;

import org.springframework.context.ApplicationEvent;

public class AuctionClosedEvent extends ApplicationEvent {

    public AuctionClosedEvent(Object source) {
        super(source);
    }
}
