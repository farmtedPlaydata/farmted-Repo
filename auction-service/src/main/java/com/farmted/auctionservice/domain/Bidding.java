package com.farmted.auctionservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Builder @Getter
public class Bidding extends TimeStamp {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long biddingId;
    private String biddingUuid;

    private Long biddingPrice; // 일반 입찰
    private Long biddingAutoPrice; // 자동 입찰
    private LocalDateTime biddingTime; // 응찰 시간

    private String memberUuid; // 응찰자
    private String boardUuid;

    @PrePersist
    public void createUuid(){
        this.biddingUuid= UUID.randomUUID().toString();
    }
}
