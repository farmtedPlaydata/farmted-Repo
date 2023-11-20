package com.farmted.auctionservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Builder
public class Auction extends TimeStamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="auction_id")
    private Long auction_id;

    @Column(unique = true)
    private String auctionUuid;

    private int auctionPrice;

    private String auctionBidder;

    private String auctionBuyer;

    private String auctionDeadline;

    private Boolean auctionStatus; // 0: 경매중, 1:  경매 종료

    private String member_uuid;

    private String board_uuid;

    @PrePersist
    public void createUuid(){
        auctionUuid = UUID.randomUUID().toString();
        auctionStatus =true;
    }

}
