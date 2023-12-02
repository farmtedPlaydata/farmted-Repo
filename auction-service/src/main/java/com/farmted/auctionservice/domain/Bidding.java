package com.farmted.auctionservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor @AllArgsConstructor
public class Bidding extends TimeStamp {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long biddingListId;
    private String biddingUuid;

    private Long biddingPrice;
    private Long biddingAutoPrice;
    private LocalDateTime biddingTime;
    private String memberUuid;
    private String boardUuid;

}
