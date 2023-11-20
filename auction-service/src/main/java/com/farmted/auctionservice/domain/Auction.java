package com.farmted.auctionservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Auction extends TimeStamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int price;

    private String bidder;

    private String buyer;

    private String deadline;

    private Boolean status; // 0: 경매중, 1:  경매 종료

    private String member_uuid;

    private String board_uuid;

}
