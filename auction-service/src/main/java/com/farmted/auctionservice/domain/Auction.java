package com.farmted.auctionservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    private int auctionPrice; // 가격

    private String auctionBuyer;  // 낙찰자

    private LocalDate auctionDeadline; // 경매 종료 시간

    private Boolean auctionStatus; // 0: 경매중, 1:  경매 종료

    private String memberUuid;

    private String boardUuid;

    private String productUuid;

    @PrePersist
    public void createUuid(){
        auctionUuid = UUID.randomUUID().toString();
        auctionStatus =true;
    }

}
